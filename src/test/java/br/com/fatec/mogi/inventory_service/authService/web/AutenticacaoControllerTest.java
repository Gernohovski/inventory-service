package br.com.fatec.mogi.inventory_service.authService.web;

import br.com.fatec.mogi.inventory_service.InventoryServiceApplication;
import br.com.fatec.mogi.inventory_service.authService.service.AutorizacaoService;
import br.com.fatec.mogi.inventory_service.authService.web.dto.request.CadastrarUsuarioRequestDTO;
import br.com.fatec.mogi.inventory_service.authService.web.dto.request.LoginRequestDTO;
import br.com.fatec.mogi.inventory_service.authService.web.dto.request.RefreshTokenRequestDTO;
import br.com.fatec.mogi.inventory_service.authService.web.dto.response.LoginResponseDTO;
import br.com.fatec.mogi.inventory_service.authService.web.dto.response.RefreshTokenResponseDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = InventoryServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AutenticacaoControllerTest {

	@LocalServerPort
	private int port;

	@MockitoBean
	AutorizacaoService autorizacaoService;

	@BeforeEach
	void setUp() {
		Mockito.doNothing().when(autorizacaoService).autorizar(ArgumentMatchers.any(), ArgumentMatchers.any());
	}

	@Test
	@DisplayName("Deve realizar o login de um usuário com sucesso")
	void deveRealizarLoginUsuarioComSucesso() {
		String email = "emailteste@gmail.com";
		String senha = "Senha123";
		cadastrarUsuario(email, senha);
		LoginRequestDTO dto = LoginRequestDTO.builder().senha(senha).email(email).build();

		var responseDTO = RestAssured.given()
			.port(port)
			.contentType(ContentType.JSON)
			.body(dto)
			.log()
			.all()
			.when()
			.post("/auth-service/v1/autenticacao/login")
			.then()
			.statusCode(200)
			.extract()
			.body()
			.as(LoginResponseDTO.class);

		assertNotNull(responseDTO);
	}

	@Test
	@DisplayName("Deve retornar exceção ao tentar realizar login para usuário não cadastrado")
	void deveRetornarExcecaoAoTentarRealizarLoginUsuarioNaoCadastrado() {
		LoginRequestDTO dto = LoginRequestDTO.builder()
			.senha("senha")
			.email(UUID.randomUUID().toString().concat("@gmail.com"))
			.build();

		var errorMessage = RestAssured.given()
			.port(port)
			.contentType(ContentType.JSON)
			.body(dto)
			.log()
			.all()
			.when()
			.post("/auth-service/v1/autenticacao/login")
			.then()
			.statusCode(400)
			.extract()
			.body()
			.asString();

		assertEquals("E-mail ou senha inválidos.", errorMessage);
	}

	@Test
	@DisplayName("Deve retornar exceção ao tentar realizar login com senha inválida")
	void deveRetornarExcecaoAoTentarRealizarLoginSenhaInvalida() {
		String email = UUID.randomUUID().toString().concat("@gmail.com");
		cadastrarUsuario(email, "Senha1234");
		LoginRequestDTO dto = LoginRequestDTO.builder().senha("Senha123").email(email).build();
		var errorMessage = RestAssured.given()
			.port(port)
			.contentType(ContentType.JSON)
			.body(dto)
			.log()
			.all()
			.when()
			.post("/auth-service/v1/autenticacao/login")
			.then()
			.statusCode(400)
			.extract()
			.body()
			.asString();

		assertEquals("E-mail ou senha inválidos.", errorMessage);
	}

	@Test
	@DisplayName("Deve atualizar token do usuário com sucesso")
	void deveAtualizarTokenUsuarioComSucesso() {
		String email = UUID.randomUUID().toString().concat("@gmail.com");
		String senha = "Senha123";
		cadastrarUsuario(email, senha);
		var tokens = fazerLogin(email, senha);
		RefreshTokenRequestDTO dto = RefreshTokenRequestDTO.builder().refreshToken(tokens.getRefreshToken()).build();
		var responseDTO = RestAssured.given()
			.port(port)
			.contentType(ContentType.JSON)
			.body(dto)
			.header("X-ACCESS-TOKEN", "token")
			.log()
			.all()
			.when()
			.put("/auth-service/v1/autenticacao/refresh")
			.then()
			.statusCode(200)
			.extract()
			.body()
			.as(RefreshTokenResponseDTO.class);

		assertNotNull(responseDTO);
		assertNotEquals(tokens.getAccessToken(), responseDTO.getAccessToken());
		assertNotEquals(tokens.getRefreshToken(), responseDTO.getRefreshToken());
	}

	@Test
	@DisplayName("Deve lançar exceção ao atualizar token inválido")
	void deveLancarExcecaoAtualizarTokenInvalido() {
		var errorMessage = RestAssured.given()
			.port(port)
			.contentType(ContentType.JSON)
			.body(new RefreshTokenRequestDTO(UUID.randomUUID().toString()))
			.header("X-ACCESS-TOKEN", "token")
			.log()
			.all()
			.when()
			.put("/auth-service/v1/autenticacao/refresh")
			.then()
			.statusCode(401)
			.extract()
			.body()
			.asString();

		assertEquals("Usuário não autenticado.", errorMessage);
	}

	@Test
	@DisplayName("Deve fazer logout do usuário com sucesso")
	void deveFazerLogoutUsuarioComSucesso() {
		String email = UUID.randomUUID().toString().concat("@gmail.com");
		String senha = "Senha123";
		cadastrarUsuario(email, senha);
		var tokens = fazerLogin(email, senha);
		RestAssured.given()
			.port(port)
			.contentType(ContentType.JSON)
			.header("X-ACCESS-TOKEN", tokens.getAccessToken())
			.header("X-REFRESH-TOKEN", tokens.getRefreshToken())
			.log()
			.all()
			.when()
			.put("/auth-service/v1/autenticacao/logout")
			.then()
			.statusCode(204);
	}

	private LoginResponseDTO fazerLogin(String email, String senha) {
		LoginRequestDTO dto = LoginRequestDTO.builder().senha(senha).email(email).build();
		return RestAssured.given()
			.port(port)
			.contentType(ContentType.JSON)
			.body(dto)
			.log()
			.all()
			.when()
			.post("/auth-service/v1/autenticacao/login")
			.then()
			.statusCode(200)
			.extract()
			.body()
			.as(LoginResponseDTO.class);
	}

	private void cadastrarUsuario(String email, String senha) {
		CadastrarUsuarioRequestDTO cadastrarUsuarioRequestDTO = CadastrarUsuarioRequestDTO.builder()
			.nome("Usuario teste")
			.email(email)
			.senha(senha)
			.funcaoId(1L)
			.build();
		RestAssured.given()
			.port(port)
			.contentType(ContentType.JSON)
			.header("X-ACCESS-TOKEN", "token")
			.body(cadastrarUsuarioRequestDTO)
			.log()
			.all()
			.when()
			.post("/auth-service/v1/usuarios")
			.then()
			.statusCode(201);
	}

}

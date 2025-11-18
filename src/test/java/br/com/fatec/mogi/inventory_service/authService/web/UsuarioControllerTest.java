package br.com.fatec.mogi.inventory_service.authService.web;

import br.com.fatec.mogi.inventory_service.InventoryServiceApplication;
import br.com.fatec.mogi.inventory_service.authService.service.AutorizacaoService;
import br.com.fatec.mogi.inventory_service.authService.utils.GeradorCodigo;
import br.com.fatec.mogi.inventory_service.authService.web.dto.request.AlterarSenhaRequestDTO;
import br.com.fatec.mogi.inventory_service.authService.web.dto.request.CadastrarUsuarioRequestDTO;
import br.com.fatec.mogi.inventory_service.authService.web.dto.request.LoginRequestDTO;
import br.com.fatec.mogi.inventory_service.authService.web.dto.request.SolicitarResetSenhaRequestDTO;
import br.com.fatec.mogi.inventory_service.authService.web.dto.response.LoginResponseDTO;
import br.com.fatec.mogi.inventory_service.authService.web.dto.response.SolicitarResetSenhaResponseDTO;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = InventoryServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UsuarioControllerTest {

	@LocalServerPort
	private int port;

	@MockitoBean
	private GeradorCodigo geradorCodigo;

	@MockitoBean
	AutorizacaoService autorizacaoService;

	@BeforeEach
	void setUp() {
		Mockito.doNothing().when(autorizacaoService).autorizar(ArgumentMatchers.any(), ArgumentMatchers.any());
	}

	@Test
	@DisplayName("Não deve cadastrar usuário com e-mail inválido")
	void naoDeveCadastrarUsuarioComEmailInvalido() {
		CadastrarUsuarioRequestDTO dto = CadastrarUsuarioRequestDTO.builder()
			.nome("Usuario teste")
			.email("")
			.senha("Senha123")
			.funcaoId(1L)
			.build();
		var errorMessage = RestAssured.given()
			.port(port)
			.contentType(ContentType.JSON)
			.header("X-ACCESS-TOKEN", "token")

			.body(dto)
			.log()
			.all()
			.when()
			.post("/auth-service/v1/usuarios")
			.then()
			.statusCode(400)
			.extract()
			.body()
			.asString();

		assertEquals("O e-mail não pode ser nulo ou vazio.", errorMessage);
	}

	@Test
	@DisplayName("Não deve cadastrar usuário com senha inválida")
	void naoDeveCadastrarUsuarioComSenhaInvalida() {
		CadastrarUsuarioRequestDTO dto = CadastrarUsuarioRequestDTO.builder()
			.nome("Usuario teste")
			.email("email@gmail.com")
			.senha("")
			.funcaoId(1L)
			.build();
		var errorMessage = RestAssured.given()
			.port(port)
			.contentType(ContentType.JSON)
			.header("X-ACCESS-TOKEN", "token")

			.body(dto)
			.log()
			.all()
			.when()
			.post("/auth-service/v1/usuarios")
			.then()
			.statusCode(400)
			.extract()
			.body()
			.asString();

		assertEquals("A senha deve ter pelo menos 8 caracteres.", errorMessage);
	}

	@Test
	@DisplayName("Não deve cadastrar usuário com e-mail já cadastrado")
	void naoDeveCadastrarUsuarioComEmailJaCadastrado() {
		CadastrarUsuarioRequestDTO dto = CadastrarUsuarioRequestDTO.builder()
			.nome("Usuario teste")
			.email("email@gmail.com")
			.senha("Senha123")
			.funcaoId(1L)
			.build();
		RestAssured.given()
			.port(port)
			.contentType(ContentType.JSON)
			.header("X-ACCESS-TOKEN", "token")

			.body(dto)
			.log()
			.all()
			.when()
			.post("/auth-service/v1/usuarios")
			.then()
			.statusCode(201);

		var errorMessage = RestAssured.given()
			.port(port)
			.contentType(ContentType.JSON)
			.header("X-ACCESS-TOKEN", "token")

			.body(dto)
			.log()
			.all()
			.when()
			.post("/auth-service/v1/usuarios")
			.then()
			.statusCode(400)
			.extract()
			.body()
			.asString();

		assertEquals("E-mail já utilizado.", errorMessage);
	}

	@Test
	@DisplayName("Deve solicitar redefinição senha usuário com sucesso")
	void deveSolicitarRedefinicaoSenhaUsuarioComSucesso() {
		String email = UUID.randomUUID().toString().concat("@gmail.com");
		String senha = "Senha123";
		cadastrarUsuario(email, senha);
		SolicitarResetSenhaRequestDTO dto = SolicitarResetSenhaRequestDTO.builder().email(email).build();

		var solicitarResetSenhaResponseDTO = RestAssured.given()
			.port(port)
			.contentType(ContentType.JSON)
			.body(dto)
			.log()
			.all()
			.when()
			.put("/auth-service/v1/usuarios/solicitar-redefinicao-senha")
			.then()
			.statusCode(200)
			.extract()
			.body()
			.as(SolicitarResetSenhaResponseDTO.class);

		assertTrue(solicitarResetSenhaResponseDTO.isEmailEnviado());
	}

	@Test
	@DisplayName("Deve retornar erro ao tentar solicitar redefinição de senha para usuário inválido")
	void deveRetornarErroTentarSolicitarRedefinicaoSenhaUsuarioInvalido() {
		SolicitarResetSenhaRequestDTO dto = SolicitarResetSenhaRequestDTO.builder()
			.email(UUID.randomUUID().toString().concat("@gmail.com"))
			.build();

		var errorMessage = RestAssured.given()
			.port(port)
			.contentType(ContentType.JSON)
			.body(dto)
			.log()
			.all()
			.when()
			.put("/auth-service/v1/usuarios/solicitar-redefinicao-senha")
			.then()
			.statusCode(400)
			.extract()
			.body()
			.asString();

		assertEquals("Usuário não encontrado.", errorMessage);
	}

	@Test
	@DisplayName("Deve redefinir senha do usuário com sucesso")
	void deveRedefinirSenhaUsuarioComSucesso() {
		String codigoEsperado = "123456";
		String email = UUID.randomUUID().toString().concat("@gmail.com");
		String senha = "Senha123";
		String novaSenha = "Senha182391823";
		cadastrarUsuario(email, senha);
		when(geradorCodigo.gerarCodigo()).thenReturn(codigoEsperado);
		SolicitarResetSenhaRequestDTO solicitarResetSenhaDto = SolicitarResetSenhaRequestDTO.builder()
			.email(email)
			.build();
		RestAssured.given()
			.port(port)
			.contentType(ContentType.JSON)
			.body(solicitarResetSenhaDto)
			.log()
			.all()
			.when()
			.put("/auth-service/v1/usuarios/solicitar-redefinicao-senha")
			.then()
			.statusCode(200)
			.extract()
			.body();

		AlterarSenhaRequestDTO alterarSenhaRequestDTO = AlterarSenhaRequestDTO.builder()
			.codigo(codigoEsperado)
			.novaSenha(novaSenha)
			.build();

		RestAssured.given()
			.port(port)
			.contentType(ContentType.JSON)
			.body(alterarSenhaRequestDTO)
			.log()
			.all()
			.when()
			.put("/auth-service/v1/usuarios/alterar-senha")
			.then()
			.statusCode(204)
			.extract()
			.body();

		var tokens = fazerLogin(email, novaSenha);

		assertNotNull(tokens);
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
			.body(cadastrarUsuarioRequestDTO)
			.header("X-ACCESS-TOKEN", "token")

			.log()
			.all()
			.when()
			.post("/auth-service/v1/usuarios")
			.then()
			.statusCode(201);
	}

}

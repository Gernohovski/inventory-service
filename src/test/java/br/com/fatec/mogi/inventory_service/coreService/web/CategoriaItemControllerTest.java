package br.com.fatec.mogi.inventory_service.coreService.web;

import br.com.fatec.mogi.inventory_service.InventoryServiceApplication;
import br.com.fatec.mogi.inventory_service.authService.service.AutorizacaoService;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarCategoriaItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.BuscarCategoriasResponseDTO;
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

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = InventoryServiceApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CategoriaItemControllerTest {

	@LocalServerPort
	private int port;

	@MockitoBean
	AutorizacaoService autorizacaoService;

	@BeforeEach
	void setUp() {
		Mockito.doNothing().when(autorizacaoService).autorizar(ArgumentMatchers.any(), ArgumentMatchers.any());
	}

	@Test
	@DisplayName("Deve buscar por categorias com sucesso")
	void deveBuscarCategoriaComSucesso() {
		var responseDTO = RestAssured.given()
			.port(port)
			.contentType(ContentType.JSON)
			.log()
			.all()
			.header("X-ACCESS-TOKEN", "token")
			.when()
			.get("/core-service/v1/categorias")
			.then()
			.statusCode(200)
			.extract()
			.body()
			.as(BuscarCategoriasResponseDTO.class);

		assertTrue(responseDTO.getCategorias().toString().contains("MESA"));
		assertTrue(responseDTO.getCategorias().toString().contains("CADEIRA"));
		assertTrue(responseDTO.getCategorias().toString().contains("COMPUTADOR"));
	}

	@Test
	@DisplayName("Deve cadastar categoria com sucesso")
	void deveCadastrarCategoriaSucesso() {
		var dto = CadastrarCategoriaItemRequestDTO.builder().nome("MOUSE").build();
		RestAssured.given()
			.port(port)
			.contentType(ContentType.JSON)
			.log()
			.all()
			.body(dto)
			.header("X-ACCESS-TOKEN", "token")
			.when()
			.post("/core-service/v1/categorias")
			.then()
			.statusCode(201);
	}

	@Test
	@DisplayName("Deve retornar erro ao cadastrar categoria já cadastrada")
	void deveRetornarErroCadastrarCategoriaJaCadastrada() {
		var dto = CadastrarCategoriaItemRequestDTO.builder().nome("MESA").build();
		RestAssured.given()
			.port(port)
			.contentType(ContentType.JSON)
			.log()
			.all()
			.body(dto)
			.header("X-ACCESS-TOKEN", "token")
			.when()
			.post("/core-service/v1/categorias")
			.then()
			.statusCode(400);
	}

}

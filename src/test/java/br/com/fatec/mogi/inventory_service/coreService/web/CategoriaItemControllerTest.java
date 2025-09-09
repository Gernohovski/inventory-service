package br.com.fatec.mogi.inventory_service.coreService.web;

import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarCategoriaItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.BuscarCategoriasResponseDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CategoriaItemControllerTest {

	@LocalServerPort
	private int port;

	@Test
	@DisplayName("Deve buscar por categorias com sucesso")
	void deveBuscarCategoriaComSucesso() {
		var responseDTO = RestAssured.given()
			.port(port)
			.contentType(ContentType.JSON)
			.log()
			.all()
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
			.when()
			.post("/core-service/v1/categorias")
			.then()
			.statusCode(400);
	}

}

package br.com.fatec.mogi.inventory_service.coreService.web;

import br.com.fatec.mogi.inventory_service.coreService.web.response.BuscarLocalizacaoResponseDTO;
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
public class LocalizacaoControllerTest {

	@LocalServerPort
	private int port;

	@Test
	@DisplayName("Deve buscar localizações com sucesso")
	void deveBuscarLocalizacoesComSucesso() {
		var responseDTO = RestAssured.given()
			.port(port)
			.contentType(ContentType.JSON)
			.log()
			.all()
			.when()
			.get("/core-service/v1/localizacao")
			.then()
			.statusCode(200)
			.extract()
			.body()
			.as(BuscarLocalizacaoResponseDTO.class);

		assertTrue(responseDTO.getLocalizacao().toString().contains("LAB01"));
		assertTrue(responseDTO.getLocalizacao().toString().contains("LAB02"));
		assertTrue(responseDTO.getLocalizacao().toString().contains("LAB03"));
	}

}

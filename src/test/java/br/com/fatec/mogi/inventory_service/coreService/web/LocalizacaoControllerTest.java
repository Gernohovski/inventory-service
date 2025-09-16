package br.com.fatec.mogi.inventory_service.coreService.web;

import br.com.fatec.mogi.inventory_service.InventoryServiceApplication;
import br.com.fatec.mogi.inventory_service.authService.service.AutorizacaoService;
import br.com.fatec.mogi.inventory_service.coreService.web.response.BuscarLocalizacaoResponseDTO;
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
public class LocalizacaoControllerTest {

	@LocalServerPort
	private int port;

	@MockitoBean
	AutorizacaoService autorizacaoService;

	@BeforeEach
	void setUp() {
		Mockito.doNothing().when(autorizacaoService).autorizar(ArgumentMatchers.any(), ArgumentMatchers.any());
	}

	@Test
	@DisplayName("Deve buscar localizações com sucesso")
	void deveBuscarLocalizacoesComSucesso() {
		var responseDTO = RestAssured.given()
			.port(port)
			.contentType(ContentType.JSON)
				.header("X-ACCESS-TOKEN", "token")

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

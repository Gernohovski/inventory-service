package br.com.fatec.mogi.inventory_service.coreService.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.AssertionsKt.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class LocalizacaoServiceTest {

	@Autowired
	private LocalizacaoService localizacaoService;

	@Test
	@DisplayName("Deve listar todas as localizaçẽos com sucesso")
	void deveListarTodasLocalizacoesComSucesso() {
		var localizacoes = localizacaoService.buscar();
		assertNotNull(localizacoes);
		assertTrue(localizacoes.getLocalizacao().toString().contains("LAB01"));
		assertTrue(localizacoes.getLocalizacao().toString().contains("LAB02"));
		assertTrue(localizacoes.getLocalizacao().toString().contains("LAB03"));
	}

}

package br.com.fatec.mogi.inventory_service.coreService.service;

import br.com.fatec.mogi.inventory_service.coreService.repository.LocalizacaoRepository;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarLocalizacaoRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.AssertionsKt.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class LocalizacaoServiceTest {

	@Autowired
	private LocalizacaoService localizacaoService;

	@Autowired
	private LocalizacaoRepository localizacaoRepository;

	@Test
	@DisplayName("Deve listar todas as localizaçẽos com sucesso")
	void deveListarTodasLocalizacoesComSucesso() {
		var localizacoes = localizacaoService.buscar();
		assertNotNull(localizacoes);
		assertTrue(localizacoes.getLocalizacao().toString().contains("LAB01"));
		assertTrue(localizacoes.getLocalizacao().toString().contains("LAB02"));
		assertTrue(localizacoes.getLocalizacao().toString().contains("LAB03"));
	}

	@Test
	@DisplayName("Deve cadastrar localização com sucesso")
	void deveCadastrarLocalizacaoComSucesso() {
		var dto = CadastrarLocalizacaoRequestDTO.builder().andar("1").nomeSala("SALA MAKER").build();
		localizacaoService.cadastrar(dto);
		var localizacoes = localizacaoRepository.findAll();
		assertTrue(localizacoes.toString().contains("SALA MAKER"));
	}

	@Test
	@DisplayName("Deve buscar localizações paginadas com sucesso")
	void deveBuscarLocalizacoesPaginadasComSucesso() {
		var pageable = PageRequest.of(0, 10);
		var resultado = localizacaoService.buscarPaginado(pageable);

		assertNotNull(resultado);
		assertTrue(resultado.getTotalElements() >= 3);
		assertTrue(resultado.getContent().size() > 0);
		assertEquals(0, resultado.getPage());
		assertEquals(10, resultado.getSize());
		assertTrue(resultado.getTotalPages() >= 1);

		var localizacoes = resultado.getContent();
		assertTrue(localizacoes.stream().anyMatch(l -> l.getNomeSala().equals("LAB01")));
		assertTrue(localizacoes.stream().anyMatch(l -> l.getNomeSala().equals("LAB02")));
		assertTrue(localizacoes.stream().anyMatch(l -> l.getNomeSala().equals("LAB03")));
	}

}

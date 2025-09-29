package br.com.fatec.mogi.inventory_service.coreService.service;

import br.com.fatec.mogi.inventory_service.coreService.domain.exception.LocalizacaoNaoEncontradaException;
import br.com.fatec.mogi.inventory_service.coreService.repository.LocalizacaoRepository;
import br.com.fatec.mogi.inventory_service.coreService.web.request.AtualizarLocalizacaoRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarLocalizacaoRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
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
	@DisplayName("Deve atualizar localização com sucesso")
	void deveAtualizarLocalizacaoComSucesso() {

		var dto = AtualizarLocalizacaoRequestDTO.builder().andar("2").nomeSala("SALA MAKER ATUALIZADA").build();

		var localizacoesAntes = localizacaoRepository.findAll();
		var localizacaoExistente = localizacoesAntes.stream()
			.filter(l -> l.getNomeSala().equals("SALA MAKER"))
			.findFirst()
			.orElse(null);

		if (localizacaoExistente != null) {
			localizacaoService.atualizar(dto, localizacaoExistente.getId());

			var localizacoesDepois = localizacaoRepository.findAll();
			var localizacaoAtualizada = localizacoesDepois.stream()
				.filter(l -> l.getId().equals(localizacaoExistente.getId()))
				.findFirst()
				.orElse(null);

			assertNotNull(localizacaoAtualizada);
			assertEquals("SALA MAKER ATUALIZADA", localizacaoAtualizada.getNomeSala());
			assertEquals("2", localizacaoAtualizada.getAndar());
		}
	}

	@Test
	@DisplayName("Deve retornar erro ao atualizar localização inexistente")
	void deveRetornarErroAoAtualizarLocalizacaoInexistente() {
		var dto = AtualizarLocalizacaoRequestDTO.builder().andar("2").nomeSala("SALA MAKER ATUALIZADA").build();

		assertThrows(LocalizacaoNaoEncontradaException.class, () -> localizacaoService.atualizar(dto, 999L));
	}

}

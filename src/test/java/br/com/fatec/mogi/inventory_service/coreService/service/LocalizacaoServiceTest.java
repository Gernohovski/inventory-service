package br.com.fatec.mogi.inventory_service.coreService.service;

import br.com.fatec.mogi.inventory_service.coreService.domain.exception.LocalizacaoNaoEncontradaException;
import br.com.fatec.mogi.inventory_service.coreService.repository.ItemRepository;
import br.com.fatec.mogi.inventory_service.coreService.repository.LocalizacaoRepository;
import br.com.fatec.mogi.inventory_service.coreService.web.request.AtualizarLocalizacaoRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarItemRequestDTO;
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

	@Autowired
	private ItemService itemService;

	@Autowired
	private ItemRepository itemRepository;

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

	@Test
	@DisplayName("Deve deletar localização com sucesso")
	void deveDeletarLocalizacaoComSucesso() {
		var dto = CadastrarLocalizacaoRequestDTO.builder().andar("1").nomeSala("SALA DELETAR").build();
		localizacaoService.cadastrar(dto);

		var localizacoesAntes = localizacaoRepository.findAll();
		var localizacaoDeletar = localizacoesAntes.stream()
			.filter(localizacao -> localizacao.getNomeSala().equals("SALA DELETAR"))
			.findFirst()
			.orElse(null);
		assertNotNull(localizacaoDeletar);
		localizacaoService.deletar(localizacaoDeletar.getId());
		var localizacoesDepois = localizacaoRepository.findAll();
		assertTrue(
				localizacoesDepois.stream().noneMatch(localizacao -> localizacao.getNomeSala().equals("SALA DELETAR")));
	}

	@Test
	@DisplayName("Deve retornar erro ao tentar deletar localização inexistente")
	void deveRetornarErroAoTentarDeletarLocalizacaoInexistente() {
		assertThrows(LocalizacaoNaoEncontradaException.class, () -> localizacaoService.deletar(999L));
	}

	@Test
	@DisplayName("Deve calcular quantidade de itens por localização corretamente")
	void deveCalcularQuantidadeItensCorretamente() {
		var localizacaoDto = CadastrarLocalizacaoRequestDTO.builder()
			.andar("3")
			.nomeSala("SALA TESTE QUANTIDADE")
			.build();
		localizacaoService.cadastrar(localizacaoDto);
		var localizacoes = localizacaoRepository.findAll();
		var localizacaoTeste = localizacoes.stream()
			.filter(l -> l.getNomeSala().equals("SALA TESTE QUANTIDADE"))
			.findFirst()
			.orElse(null);
		assertNotNull(localizacaoTeste);
		var localizacaoInicial = localizacaoRepository.findById(localizacaoTeste.getId()).orElse(null);
		assertNotNull(localizacaoInicial);
		assertEquals(0L, localizacaoInicial.getQuantidadeItens());
		for (int i = 1; i <= 3; i++) {
			var itemDto = CadastrarItemRequestDTO.builder()
				.nomeItem("Item Teste Quantidade " + i)
				.descricaoCurta("Item " + i)
				.descricaoDetalhada("Item para teste de quantidade " + i)
				.numeroSerie("NS-QTDE-" + i)
				.codigoItem("COD-QTDE-" + System.currentTimeMillis() + "-" + i)
				.notaFiscal("NF-QTDE-" + i)
				.categoriaItemId(1L)
				.localizacaoId(localizacaoTeste.getId())
				.statusItemId(1L)
				.tipoEntradaId(1L)
				.build();
			itemService.cadastrarItem(itemDto);
		}
		var localizacaoAtualizada = localizacaoRepository.findById(localizacaoTeste.getId()).orElse(null);
		assertNotNull(localizacaoAtualizada);
		assertEquals(3L, localizacaoAtualizada.getQuantidadeItens());
		var itens = itemRepository.findAll();
		var itemParaDeletar = itens.stream()
			.filter(i -> i.getCodigoItem().startsWith("COD-QTDE-"))
			.findFirst()
			.orElse(null);
		assertNotNull(itemParaDeletar);
		itemService.deletar(itemParaDeletar.getId());
		var localizacaoAposDeletar = localizacaoRepository.findById(localizacaoTeste.getId()).orElse(null);
		assertNotNull(localizacaoAposDeletar);
		assertEquals(2L, localizacaoAposDeletar.getQuantidadeItens());
	}

	@Test
	@DisplayName("Deve retornar quantidade zero para localização sem itens")
	void deveRetornarQuantidadeZeroParaLocalizacaoSemItens() {
		var localizacaoDto = CadastrarLocalizacaoRequestDTO.builder()
			.andar("4")
			.nomeSala("SALA VAZIA TESTE")
			.build();
		localizacaoService.cadastrar(localizacaoDto);
		var localizacoes = localizacaoRepository.findAll();
		var localizacaoVazia = localizacoes.stream()
			.filter(l -> l.getNomeSala().equals("SALA VAZIA TESTE"))
			.findFirst()
			.orElse(null);
		assertNotNull(localizacaoVazia);
		assertEquals(0L, localizacaoVazia.getQuantidadeItens());
	}

}

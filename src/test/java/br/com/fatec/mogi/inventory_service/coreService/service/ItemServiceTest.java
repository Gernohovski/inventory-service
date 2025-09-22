package br.com.fatec.mogi.inventory_service.coreService.service;

import br.com.fatec.mogi.inventory_service.coreService.domain.exception.*;
import br.com.fatec.mogi.inventory_service.coreService.repository.ItemRepository;
import br.com.fatec.mogi.inventory_service.coreService.service.impl.ItemServiceImpl;
import br.com.fatec.mogi.inventory_service.coreService.web.request.AtualizarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.ConsultarItemRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class ItemServiceTest {

	@Autowired
	private ItemServiceImpl itemService;

	@Autowired
	private ItemRepository itemRepository;

	@Test
	@DisplayName("Deve cadastrar item com sucesso")
	void deveCadastrarItemComSucesso() {
		var dto = CadastrarItemRequestDTO.builder()
			.nomeItem("Cadeira Gamer")
			.descricaoCurta("Cadeira")
			.descricaoDetalhada("Cadeira confortável")
			.numeroSerie("NS-123")
			.codigoItem("COD-UNICO-1")
			.notaFiscal("NF-001")
			.categoriaItemId(1L)
			.localizacaoId(1L)
			.statusItemId(1L)
			.tipoEntradaId(1L)
			.build();

		itemService.cadastrarItem(dto);

		assertTrue(itemRepository.existsByCodigoItem("COD-UNICO-1"));
	}

	@Test
	@DisplayName("Deve lançar erro ao cadastrar item com código já cadastrado")
	void deveLancarErroCodigoJaCadastrado() {
		var dto = CadastrarItemRequestDTO.builder()
			.nomeItem("Cadeira Escritório")
			.descricaoCurta("Cadeira")
			.descricaoDetalhada("Cadeira de escritório")
			.numeroSerie("NS-124")
			.codigoItem("COD-DUP-1")
			.notaFiscal("NF-002")
			.categoriaItemId(1L)
			.localizacaoId(1L)
			.statusItemId(1L)
			.tipoEntradaId(1L)
			.build();

		itemService.cadastrarItem(dto);

		assertThrows(ItemJaCadastradoException.class, () -> {
			itemService.cadastrarItem(dto);
		});
	}

	@Test
	@DisplayName("Deve lançar erro ao cadastrar item com categoria inexistente")
	void deveLancarErroCategoriaInexistente() {
		var dto = CadastrarItemRequestDTO.builder()
			.nomeItem("Item Teste")
			.descricaoCurta("Desc")
			.descricaoDetalhada("Detalhe")
			.numeroSerie("NS-125")
			.codigoItem("COD-CAT-ERR")
			.notaFiscal("NF-003")
			.categoriaItemId(999L)
			.localizacaoId(1L)
			.statusItemId(1L)
			.tipoEntradaId(1L)
			.build();

		assertThrows(CategoriaNaoEncontradaException.class, () -> {
			itemService.cadastrarItem(dto);
		});
	}

	@Test
	@DisplayName("Deve lançar erro ao cadastrar item com tipo de entrada inexistente")
	void deveLancarErroTipoEntradaInexistente() {
		var dto = CadastrarItemRequestDTO.builder()
			.nomeItem("Item Teste")
			.descricaoCurta("Desc")
			.descricaoDetalhada("Detalhe")
			.numeroSerie("NS-126")
			.codigoItem("COD-TE-ERR")
			.notaFiscal("NF-004")
			.categoriaItemId(1L)
			.localizacaoId(1L)
			.statusItemId(1L)
			.tipoEntradaId(999L)
			.build();

		assertThrows(TipoEntradaNaoEncontradaException.class, () -> {
			itemService.cadastrarItem(dto);
		});
	}

	@Test
	@DisplayName("Deve lançar erro ao cadastrar item com status inexistente")
	void deveLancarErroStatusInexistente() {
		var dto = CadastrarItemRequestDTO.builder()
			.nomeItem("Item Teste")
			.descricaoCurta("Desc")
			.descricaoDetalhada("Detalhe")
			.numeroSerie("NS-127")
			.codigoItem("COD-ST-ERR")
			.notaFiscal("NF-005")
			.categoriaItemId(1L)
			.localizacaoId(1L)
			.statusItemId(999L)
			.tipoEntradaId(1L)
			.build();

		assertThrows(StatusItemNaoEncontradoException.class, () -> {
			itemService.cadastrarItem(dto);
		});
	}

	@Test
	@DisplayName("Deve lançar erro ao cadastrar item com localização inexistente")
	void deveLancarErroLocalizacaoInexistente() {
		var dto = CadastrarItemRequestDTO.builder()
			.nomeItem("Item Teste")
			.descricaoCurta("Desc")
			.descricaoDetalhada("Detalhe")
			.numeroSerie("NS-128")
			.codigoItem("COD-LOC-ERR")
			.notaFiscal("NF-006")
			.categoriaItemId(1L)
			.localizacaoId(999L)
			.statusItemId(1L)
			.tipoEntradaId(1L)
			.build();

		assertThrows(LocalizacaoNaoEncontradaException.class, () -> {
			itemService.cadastrarItem(dto);
		});
	}

	@Test
	@DisplayName("Deve filtrar itens sem filtros retornando página completa")
	void deveFiltrarSemFiltros() {
		var dto1 = CadastrarItemRequestDTO.builder()
			.nomeItem("Monitor 24")
			.descricaoCurta("Monitor")
			.descricaoDetalhada("Full HD")
			.numeroSerie("MN-001")
			.codigoItem("COD-LIST-1")
			.notaFiscal("NF-100")
			.categoriaItemId(1L)
			.localizacaoId(1L)
			.statusItemId(1L)
			.tipoEntradaId(1L)
			.build();
		var dto2 = CadastrarItemRequestDTO.builder()
			.nomeItem("Teclado Mecânico")
			.descricaoCurta("Teclado")
			.descricaoDetalhada("ABNT2")
			.numeroSerie("TC-002")
			.codigoItem("COD-LIST-2")
			.notaFiscal("NF-101")
			.categoriaItemId(1L)
			.localizacaoId(2L)
			.statusItemId(2L)
			.tipoEntradaId(2L)
			.build();
		itemService.cadastrarItem(dto1);
		itemService.cadastrarItem(dto2);
		var resposta = itemService.filtrarItems(ConsultarItemRequestDTO.builder().build(), PageRequest.of(0, 10));
		assertTrue(resposta.getTotalElements() >= 2);
	}

	@Test
	@DisplayName("Deve filtrar itens por nome parcialmente")
	void deveFiltrarPorNome() {
		var dto = CadastrarItemRequestDTO.builder()
			.nomeItem("Teclado Teste")
			.descricaoCurta("Periférico")
			.descricaoDetalhada("Switch azul")
			.numeroSerie("TC-003")
			.codigoItem("COD-FLT-NOME-1")
			.notaFiscal("NF-200")
			.categoriaItemId(1L)
			.localizacaoId(1L)
			.statusItemId(1L)
			.tipoEntradaId(1L)
			.build();
		itemService.cadastrarItem(dto);
		var resposta = itemService.filtrarItems(ConsultarItemRequestDTO.builder().nomeItem("tec").build(),
				PageRequest.of(0, 10));
		assertTrue(resposta.getContent().stream().anyMatch(i -> i.getNomeItem().toUpperCase().contains("TEC")));
	}

	@Test
	@DisplayName("Deve filtrar itens combinando filtros de categoria e status")
	void deveFiltrarCombinandoFiltros() {
		var filtros = ConsultarItemRequestDTO.builder().categoriaItemId(1L).statusItemId(1L).build();
		var resposta = itemService.filtrarItems(filtros, PageRequest.of(0, 10));
		assertTrue(resposta.getContent().stream().allMatch(i -> i.getCategoriaItem().getId().equals(1L)));
	}

	@Test
	@DisplayName("Deve filtrar itens por intervalo de datas")
	void deveFiltrarPorIntervaloDatas() {
		var dto = CadastrarItemRequestDTO.builder()
			.nomeItem("Mouse Gamer")
			.descricaoCurta("Periférico")
			.descricaoDetalhada("RGB")
			.numeroSerie("MS-002")
			.codigoItem("COD-FLT-DATA-1")
			.notaFiscal("NF-201")
			.categoriaItemId(1L)
			.localizacaoId(1L)
			.statusItemId(1L)
			.tipoEntradaId(1L)
			.build();
		itemService.cadastrarItem(dto);
		var agora = java.time.LocalDateTime.now();
		var filtros = ConsultarItemRequestDTO.builder()
			.dataCadastroInicio(agora.minusMinutes(5))
			.dataCadastroFim(agora.plusMinutes(5))
			.build();
		var resposta = itemService.filtrarItems(filtros, PageRequest.of(0, 10));
		assertTrue(resposta.getTotalElements() > 0);
	}

	@Test
	@DisplayName("Deve atualizar item com sucesso")
	void deveAtualizarItemComSucesso() {
		var dtoOriginal = CadastrarItemRequestDTO.builder()
			.nomeItem("Item Original Unit")
			.descricaoCurta("Descrição original")
			.descricaoDetalhada("Descrição detalhada original")
			.numeroSerie("SN-UNIT-ORIGINAL")
			.codigoItem("COD-UNIT-UPDATE-1")
			.notaFiscal("NF-UNIT-ORIGINAL")
			.categoriaItemId(1L)
			.localizacaoId(1L)
			.statusItemId(1L)
			.tipoEntradaId(1L)
			.build();

		itemService.cadastrarItem(dtoOriginal);

		var itemCriado = itemRepository.findByCodigoItem("COD-UNIT-UPDATE-1").orElseThrow();
		var itemIdOriginal = itemCriado.getId();
		var dataAlteracaoOriginal = itemCriado.getDataAlteracao();

		var dtoAtualizacao = AtualizarItemRequestDTO.builder()
			.nomeItem("Item Atualizado Unit")
			.descricaoCurta("Descrição atualizada")
			.descricaoDetalhada("Descrição detalhada atualizada")
			.numeroSerie("SN-UNIT-ATUALIZADO")
			.notaFiscal("NF-UNIT-ATUALIZADA")
			.categoriaItemId(1L)
			.localizacaoId(2L)
			.statusItemId(2L)
			.tipoEntradaId(2L)
			.build();

		itemService.atualizar(dtoAtualizacao, itemIdOriginal);

		var itemAtualizado = itemRepository.findById(itemIdOriginal).orElseThrow();

		assertTrue(itemAtualizado.getNomeItem().equals("Item Atualizado Unit"));
		assertTrue(itemAtualizado.getDescricaoCurta().equals("Descrição atualizada"));
		assertTrue(itemAtualizado.getDescricaoDetalhada().equals("Descrição detalhada atualizada"));
		assertTrue(itemAtualizado.getNumeroSerie().equals("SN-UNIT-ATUALIZADO"));
		assertTrue(itemAtualizado.getNotaFiscal().equals("NF-UNIT-ATUALIZADA"));
		assertTrue(itemAtualizado.getLocalizacao().getId().equals(2L));
		assertTrue(itemAtualizado.getStatusItem().getId().equals(2L));
		assertTrue(itemAtualizado.getTipoEntrada().getId().equals(2L));
		assertTrue(itemAtualizado.getDataAlteracao().isAfter(dataAlteracaoOriginal));
	}

	@Test
	@DisplayName("Deve atualizar apenas campos fornecidos (atualização parcial)")
	void deveAtualizarApenasCamposFornecidos() {
		var dtoOriginal = CadastrarItemRequestDTO.builder()
			.nomeItem("Item Parcial Unit")
			.descricaoCurta("Descrição original")
			.descricaoDetalhada("Detalhes originais")
			.numeroSerie("SN-UNIT-PARCIAL")
			.codigoItem("COD-UNIT-PARCIAL-1")
			.notaFiscal("NF-UNIT-PARCIAL")
			.categoriaItemId(1L)
			.localizacaoId(1L)
			.statusItemId(1L)
			.tipoEntradaId(1L)
			.build();

		itemService.cadastrarItem(dtoOriginal);

		var itemCriado = itemRepository.findByCodigoItem("COD-UNIT-PARCIAL-1").orElseThrow();
		var itemId = itemCriado.getId();

		var dtoAtualizacaoParcial = AtualizarItemRequestDTO.builder()
			.nomeItem("Nome Atualizado Unit")
			.localizacaoId(2L)
			.build();

		itemService.atualizar(dtoAtualizacaoParcial, itemId);

		var itemAtualizado = itemRepository.findById(itemId).orElseThrow();

		assertTrue(itemAtualizado.getNomeItem().equals("Nome Atualizado Unit"));
		assertTrue(itemAtualizado.getLocalizacao().getId().equals(2L));

		assertTrue(itemAtualizado.getDescricaoCurta().equals("Descrição original"));
		assertTrue(itemAtualizado.getDescricaoDetalhada().equals("Detalhes originais"));
		assertTrue(itemAtualizado.getNumeroSerie().equals("SN-UNIT-PARCIAL"));
		assertTrue(itemAtualizado.getCodigoItem().equals("COD-UNIT-PARCIAL-1"));
		assertTrue(itemAtualizado.getNotaFiscal().equals("NF-UNIT-PARCIAL"));
		assertTrue(itemAtualizado.getCategoriaItem().getId().equals(1L));
		assertTrue(itemAtualizado.getStatusItem().getId().equals(1L));
		assertTrue(itemAtualizado.getTipoEntrada().getId().equals(1L));
	}

	@Test
	@DisplayName("Deve lançar erro ao tentar atualizar item inexistente")
	void deveLancarErroItemInexistente() {
		var dtoAtualizacao = AtualizarItemRequestDTO.builder()
			.nomeItem("Item Inexistente")
			.build();

		assertThrows(ItemNaoEncontradoException.class, () -> {
			itemService.atualizar(dtoAtualizacao, 99999L);
		});
	}

	@Test
	@DisplayName("Deve lançar erro ao atualizar com código já existente")
	void deveLancarErroCodigoJaExistente() {
		var dto1 = CadastrarItemRequestDTO.builder()
			.nomeItem("Item 1 Unit")
			.codigoItem("COD-UNIT-EXISTENTE-1")
			.categoriaItemId(1L)
			.localizacaoId(1L)
			.statusItemId(1L)
			.tipoEntradaId(1L)
			.build();

		itemService.cadastrarItem(dto1);

		var dto2 = CadastrarItemRequestDTO.builder()
			.nomeItem("Item 2 Unit")
			.codigoItem("COD-UNIT-EXISTENTE-2")
			.categoriaItemId(1L)
			.localizacaoId(1L)
			.statusItemId(1L)
			.tipoEntradaId(1L)
			.build();

		itemService.cadastrarItem(dto2);

		var item2 = itemRepository.findByCodigoItem("COD-UNIT-EXISTENTE-2").orElseThrow();

		var dtoAtualizacao = AtualizarItemRequestDTO.builder()
			.codigoItem("COD-UNIT-EXISTENTE-1")
			.build();

		assertThrows(ItemJaCadastradoException.class, () -> {
			itemService.atualizar(dtoAtualizacao, item2.getId());
		});
	}

	@Test
	@DisplayName("Deve permitir atualizar com o mesmo código do item")
	void devePermitirAtualizarComMesmoCodigo() {
		var dtoOriginal = CadastrarItemRequestDTO.builder()
			.nomeItem("Item Mesmo Código")
			.codigoItem("COD-UNIT-MESMO")
			.categoriaItemId(1L)
			.localizacaoId(1L)
			.statusItemId(1L)
			.tipoEntradaId(1L)
			.build();

		itemService.cadastrarItem(dtoOriginal);

		var itemCriado = itemRepository.findByCodigoItem("COD-UNIT-MESMO").orElseThrow();

		var dtoAtualizacao = AtualizarItemRequestDTO.builder()
			.nomeItem("Item Atualizado Mesmo Código")
			.codigoItem("COD-UNIT-MESMO")
			.build();

		itemService.atualizar(dtoAtualizacao, itemCriado.getId());

		var itemAtualizado = itemRepository.findById(itemCriado.getId()).orElseThrow();
		assertTrue(itemAtualizado.getNomeItem().equals("Item Atualizado Mesmo Código"));
		assertTrue(itemAtualizado.getCodigoItem().equals("COD-UNIT-MESMO"));
	}

	@Test
	@DisplayName("Deve lançar erro ao atualizar com categoria inexistente")
	void deveLancarErroAtualizarCategoriaInexistente() {
		var dtoOriginal = CadastrarItemRequestDTO.builder()
			.nomeItem("Item Teste Categoria")
			.codigoItem("COD-UNIT-CAT-TEST")
			.categoriaItemId(1L)
			.localizacaoId(1L)
			.statusItemId(1L)
			.tipoEntradaId(1L)
			.build();

		itemService.cadastrarItem(dtoOriginal);

		var itemCriado = itemRepository.findByCodigoItem("COD-UNIT-CAT-TEST").orElseThrow();

		var dtoAtualizacao = AtualizarItemRequestDTO.builder()
			.categoriaItemId(999L)
			.build();

		assertThrows(CategoriaNaoEncontradaException.class, () -> {
			itemService.atualizar(dtoAtualizacao, itemCriado.getId());
		});
	}

	@Test
	@DisplayName("Deve lançar erro ao atualizar com localização inexistente")
	void deveLancarErroAtualizarLocalizacaoInexistente() {
		var dtoOriginal = CadastrarItemRequestDTO.builder()
			.nomeItem("Item Teste Localização")
			.codigoItem("COD-UNIT-LOC-TEST")
			.categoriaItemId(1L)
			.localizacaoId(1L)
			.statusItemId(1L)
			.tipoEntradaId(1L)
			.build();

		itemService.cadastrarItem(dtoOriginal);

		var itemCriado = itemRepository.findByCodigoItem("COD-UNIT-LOC-TEST").orElseThrow();

		var dtoAtualizacao = AtualizarItemRequestDTO.builder()
			.localizacaoId(999L)
			.build();

		assertThrows(LocalizacaoNaoEncontradaException.class, () -> {
			itemService.atualizar(dtoAtualizacao, itemCriado.getId());
		});
	}

	@Test
	@DisplayName("Deve lançar erro ao atualizar com status inexistente")
	void deveLancarErroAtualizarStatusInexistente() {
		var dtoOriginal = CadastrarItemRequestDTO.builder()
			.nomeItem("Item Teste Status")
			.codigoItem("COD-UNIT-STATUS-TEST")
			.categoriaItemId(1L)
			.localizacaoId(1L)
			.statusItemId(1L)
			.tipoEntradaId(1L)
			.build();

		itemService.cadastrarItem(dtoOriginal);

		var itemCriado = itemRepository.findByCodigoItem("COD-UNIT-STATUS-TEST").orElseThrow();

		var dtoAtualizacao = AtualizarItemRequestDTO.builder()
			.statusItemId(999L)
			.build();

		assertThrows(StatusItemNaoEncontradoException.class, () -> {
			itemService.atualizar(dtoAtualizacao, itemCriado.getId());
		});
	}

	@Test
	@DisplayName("Deve lançar erro ao atualizar com tipo de entrada inexistente")
	void deveLancarErroAtualizarTipoEntradaInexistente() {
		var dtoOriginal = CadastrarItemRequestDTO.builder()
			.nomeItem("Item Teste Tipo Entrada")
			.codigoItem("COD-UNIT-TE-TEST")
			.categoriaItemId(1L)
			.localizacaoId(1L)
			.statusItemId(1L)
			.tipoEntradaId(1L)
			.build();

		itemService.cadastrarItem(dtoOriginal);

		var itemCriado = itemRepository.findByCodigoItem("COD-UNIT-TE-TEST").orElseThrow();

		var dtoAtualizacao = AtualizarItemRequestDTO.builder()
			.tipoEntradaId(999L)
			.build();

		assertThrows(TipoEntradaNaoEncontradaException.class, () -> {
			itemService.atualizar(dtoAtualizacao, itemCriado.getId());
		});
	}

	@Test
	@DisplayName("Deve atualizar data de alteração ao atualizar item")
	void deveAtualizarDataAlteracao() {
		var dtoOriginal = CadastrarItemRequestDTO.builder()
			.nomeItem("Item Data Alteração")
			.codigoItem("COD-UNIT-DATA-ALT")
			.categoriaItemId(1L)
			.localizacaoId(1L)
			.statusItemId(1L)
			.tipoEntradaId(1L)
			.build();

		itemService.cadastrarItem(dtoOriginal);

		var itemCriado = itemRepository.findByCodigoItem("COD-UNIT-DATA-ALT").orElseThrow();
		var dataAlteracaoOriginal = itemCriado.getDataAlteracao();

		var dtoAtualizacao = AtualizarItemRequestDTO.builder()
			.nomeItem("Item Data Alteração Atualizada")
			.build();

		itemService.atualizar(dtoAtualizacao, itemCriado.getId());

		var itemAtualizado = itemRepository.findById(itemCriado.getId()).orElseThrow();
		assertTrue(itemAtualizado.getDataAlteracao().isAfter(dataAlteracaoOriginal));
	}

	@Test
	@DisplayName("Deve excluir item com sucesso")
	void deveExcluirItemComSucesso() {
		var item = CadastrarItemRequestDTO.builder()
				.nomeItem("Item Teste Tipo Entrada")
				.codigoItem(UUID.randomUUID().toString())
				.categoriaItemId(1L)
				.localizacaoId(1L)
				.statusItemId(1L)
				.tipoEntradaId(1L)
				.build();

		var itemSalvo = itemService.cadastrarItem(item);

		itemService.deletar(itemSalvo.getId());

		assertFalse(itemRepository.findById(itemSalvo.getId()).isPresent());
	}

}

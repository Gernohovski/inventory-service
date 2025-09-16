package br.com.fatec.mogi.inventory_service.coreService.service;

import br.com.fatec.mogi.inventory_service.authService.service.AutorizacaoService;
import br.com.fatec.mogi.inventory_service.coreService.domain.exception.*;
import br.com.fatec.mogi.inventory_service.coreService.repository.ItemRepository;
import br.com.fatec.mogi.inventory_service.coreService.service.impl.ItemServiceImpl;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.ConsultarItemRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

}

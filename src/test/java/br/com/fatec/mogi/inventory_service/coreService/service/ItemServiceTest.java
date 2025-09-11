package br.com.fatec.mogi.inventory_service.coreService.service;

import br.com.fatec.mogi.inventory_service.authService.service.AutorizacaoService;
import br.com.fatec.mogi.inventory_service.coreService.domain.exception.CategoriaNaoEncontradaException;
import br.com.fatec.mogi.inventory_service.coreService.domain.exception.ItemJaCadastradoException;
import br.com.fatec.mogi.inventory_service.coreService.domain.exception.LocalizacaoNaoEncontradaException;
import br.com.fatec.mogi.inventory_service.coreService.domain.exception.StatusItemNaoEncontradoException;
import br.com.fatec.mogi.inventory_service.coreService.domain.exception.TipoEntradaNaoEncontradaException;
import br.com.fatec.mogi.inventory_service.coreService.repository.ItemRepository;
import br.com.fatec.mogi.inventory_service.coreService.service.impl.ItemServiceImpl;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarItemRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

	@MockitoBean
	private AutorizacaoService autorizacaoService;

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

		itemService.cadastrarItem(dto, "token");

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

		itemService.cadastrarItem(dto, "token");

		assertThrows(ItemJaCadastradoException.class, () -> {
			itemService.cadastrarItem(dto, "token");
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
			itemService.cadastrarItem(dto, "token");
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
			itemService.cadastrarItem(dto, "token");
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
			itemService.cadastrarItem(dto, "token");
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
			itemService.cadastrarItem(dto, "token");
		});
	}

}

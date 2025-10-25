package br.com.fatec.mogi.inventory_service.coreService.service;

import br.com.fatec.mogi.inventory_service.common.web.response.CustomPageResponseDTO;
import br.com.fatec.mogi.inventory_service.coreService.domain.exception.CategoriaJaCadastradaException;
import br.com.fatec.mogi.inventory_service.coreService.domain.exception.CategoriaNaoEncontradaException;
import br.com.fatec.mogi.inventory_service.coreService.web.request.AtualizarCategoriaItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarCategoriaItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.ConsultarCategoriaItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.CategoriaItemResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class CategoriaItemServiceTest {

	@Autowired
	private CategoriaItemService categoriaItemService;

	@Test
	@DisplayName("Deve consultar todas as categorias com sucesso")
	void deveConsultarCategoriasComSucesso() {
		var categorias = categoriaItemService.buscar();
		assertTrue(categorias.getCategorias().toString().contains("MESA"));
		assertTrue(categorias.getCategorias().toString().contains("CADEIRA"));
		assertTrue(categorias.getCategorias().toString().contains("COMPUTADOR"));
	}

	@Test
	@DisplayName("Deve cadastrar um categoria com sucesso")
	void deveCadastrarCategoriaComSucesso() {
		var cadastrarCategoriaItemRequestDTO = CadastrarCategoriaItemRequestDTO.builder()
			.nome("ITENS DE ESCRITÓRIO")
			.build();
		categoriaItemService.cadastrar(cadastrarCategoriaItemRequestDTO);
		var categorias = categoriaItemService.buscar();
		assertTrue(categorias.getCategorias().toString().contains(cadastrarCategoriaItemRequestDTO.getNome()));
	}

	@Test
	@DisplayName("Deve retornar erro ao cadastrar uma categoria com nome já cadastrado")
	void deveRetornarErroCadastrarCategoriaNomeCadastrado() {
		var cadastrarCategoriaItemRequestDTO = CadastrarCategoriaItemRequestDTO.builder().nome("MESA").build();
		assertThrows(CategoriaJaCadastradaException.class, () -> {
			categoriaItemService.cadastrar(cadastrarCategoriaItemRequestDTO);
		});
	}

	@Test
	@DisplayName("Deve deletar categoria com sucesso")
	void deveDeletarCategoriaComSucesso() {
		var cadastrarCategoriaItemRequestDTO = CadastrarCategoriaItemRequestDTO.builder()
			.nome("CATEGORIA_PARA_DELETAR")
			.build();
		categoriaItemService.cadastrar(cadastrarCategoriaItemRequestDTO);

		var categorias = categoriaItemService.buscar();
		var categoriaParaDeletar = categorias.getCategorias()
			.stream()
			.filter(categoria -> categoria.getNome().equals("CATEGORIA_PARA_DELETAR"))
			.findFirst()
			.orElseThrow();

		categoriaItemService.deletar(categoriaParaDeletar.getId());

		var categoriasAposDeletar = categoriaItemService.buscar();
		assertTrue(categoriasAposDeletar.getCategorias()
			.stream()
			.noneMatch(categoria -> categoria.getNome().equals("CATEGORIA_PARA_DELETAR")));
	}

	@Test
	@DisplayName("Deve retornar erro ao tentar deletar categoria inexistente")
	void deveRetornarErroDeletarCategoriaInexistente() {
		Long idInexistente = 999999L;
		assertThrows(CategoriaNaoEncontradaException.class, () -> {
			categoriaItemService.deletar(idInexistente);
		});
	}

	@Test
	@DisplayName("Deve atualizar categoria com sucesso")
	void deveAtualizarCategoriaComSucesso() {
		var cadastrarCategoriaItemRequestDTO = CadastrarCategoriaItemRequestDTO.builder()
			.nome("CATEGORIA_PARA_ATUALIZAR")
			.build();
		categoriaItemService.cadastrar(cadastrarCategoriaItemRequestDTO);

		var categorias = categoriaItemService.buscar();
		var categoriaParaAtualizar = categorias.getCategorias()
			.stream()
			.filter(categoria -> categoria.getNome().equals("CATEGORIA_PARA_ATUALIZAR"))
			.findFirst()
			.orElseThrow();

		var atualizarCategoriaItemRequestDTO = AtualizarCategoriaItemRequestDTO.builder()
			.nome("CATEGORIA_ATUALIZADA")
			.build();
		categoriaItemService.atualizar(atualizarCategoriaItemRequestDTO, categoriaParaAtualizar.getId());

		var categoriasAposAtualizar = categoriaItemService.buscar();
		assertTrue(categoriasAposAtualizar.getCategorias()
			.stream()
			.anyMatch(categoria -> categoria.getNome().equals("CATEGORIA_ATUALIZADA")));
		assertTrue(categoriasAposAtualizar.getCategorias()
			.stream()
			.noneMatch(categoria -> categoria.getNome().equals("CATEGORIA_PARA_ATUALIZAR")));
	}

	@Test
	@DisplayName("Deve retornar erro ao tentar atualizar categoria inexistente")
	void deveRetornarErroAtualizarCategoriaInexistente() {
		Long idInexistente = 999999L;
		var atualizarCategoriaItemRequestDTO = AtualizarCategoriaItemRequestDTO.builder()
			.nome("CATEGORIA_INEXISTENTE")
			.build();
		assertThrows(CategoriaNaoEncontradaException.class, () -> {
			categoriaItemService.atualizar(atualizarCategoriaItemRequestDTO, idInexistente);
		});
	}

	@Test
	@DisplayName("Deve retornar página com categorias quando existirem dados")
	void deveRetornarPaginaComCategorias() {
		Pageable pageable = PageRequest.of(0, 10);
		CustomPageResponseDTO<CategoriaItemResponseDTO> resultado = categoriaItemService
			.buscarPaginado(new ConsultarCategoriaItemRequestDTO(), pageable);
		assertThat(resultado).isNotNull();
		assertThat(resultado.getPage()).isEqualTo(0);
		assertThat(resultado.getSize()).isEqualTo(10);
		assertThat(resultado.getTotalPages()).isEqualTo(1);
		assertThat(resultado.getContent().toString().contains("CADEIRA"));
		assertThat(resultado.getContent().toString().contains("MESA"));
		assertThat(resultado.getContent().toString().contains("COMPUTADOR"));
	}

}

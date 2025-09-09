package br.com.fatec.mogi.inventory_service.coreService.service;

import br.com.fatec.mogi.inventory_service.coreService.domain.exception.CategoriaJaCadastradaException;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarCategoriaItemRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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

}

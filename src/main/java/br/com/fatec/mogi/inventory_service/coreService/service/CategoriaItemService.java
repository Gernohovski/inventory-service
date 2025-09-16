package br.com.fatec.mogi.inventory_service.coreService.service;

import br.com.fatec.mogi.inventory_service.common.web.response.CustomPageResponseDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.AtualizarCategoriaItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarCategoriaItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.BuscarCategoriasResponseDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.CategoriaItemResponseDTO;
import org.springframework.data.domain.Pageable;

public interface CategoriaItemService {

	BuscarCategoriasResponseDTO buscar();

	void cadastrar(CadastrarCategoriaItemRequestDTO dto);

	void deletar(Long id);

	void atualizar(AtualizarCategoriaItemRequestDTO dto, Long id);

	CustomPageResponseDTO<CategoriaItemResponseDTO> buscarPaginado(Pageable pageable);

}

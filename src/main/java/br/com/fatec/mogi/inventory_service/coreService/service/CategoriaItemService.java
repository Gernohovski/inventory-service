package br.com.fatec.mogi.inventory_service.coreService.service;

import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarCategoriaItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.BuscarCategoriasResponseDTO;

public interface CategoriaItemService {

	BuscarCategoriasResponseDTO buscar(String accessToken);

	void cadastrar(CadastrarCategoriaItemRequestDTO dto, String accessToken);

}

package br.com.fatec.mogi.inventory_service.coreService.service;

import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarStatusItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.BuscarStatusItemResponseDTO;

public interface StatusItemService {

	void cadastrar(CadastrarStatusItemRequestDTO dto);

	void deletar(Long id);

	BuscarStatusItemResponseDTO listar();

}

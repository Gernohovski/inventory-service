package br.com.fatec.mogi.inventory_service.coreService.service;

import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarStatusItemRequestDTO;

public interface StatusItemService {

	void cadastrar(CadastrarStatusItemRequestDTO dto);

	void deletar(Long id);

}

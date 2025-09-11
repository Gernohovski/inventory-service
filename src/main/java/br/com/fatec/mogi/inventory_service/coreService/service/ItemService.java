package br.com.fatec.mogi.inventory_service.coreService.service;

import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarItemRequestDTO;

public interface ItemService {

	void cadastrarItem(CadastrarItemRequestDTO dto, String accessToken);

}

package br.com.fatec.mogi.inventory_service.coreService.strategy;

import br.com.fatec.mogi.inventory_service.coreService.web.request.ItemUploadRequestDTO;

public interface ValidarItemStrategy {

	void executar(ItemUploadRequestDTO dto, ValidarItemContexto contexto);

}

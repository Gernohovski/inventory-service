package br.com.fatec.mogi.inventory_service.coreService.service;

import br.com.fatec.mogi.inventory_service.common.web.response.CustomPageResponseDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.ConsultarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.ItemResponseDTO;
import org.springframework.data.domain.Pageable;

public interface ItemService {

	void cadastrarItem(CadastrarItemRequestDTO dto);

	CustomPageResponseDTO<ItemResponseDTO> filtrarItems(ConsultarItemRequestDTO dto, Pageable pageable);

}

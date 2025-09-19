package br.com.fatec.mogi.inventory_service.coreService.service;

import br.com.fatec.mogi.inventory_service.coreService.domain.model.Item;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.ConsultarItemRequestDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemService {

	void cadastrarItem(CadastrarItemRequestDTO dto);

	List<Item> filtrarItems(ConsultarItemRequestDTO dto, Pageable pageable);

}

package br.com.fatec.mogi.inventory_service.coreService.processor;

import br.com.fatec.mogi.inventory_service.coreService.domain.model.Item;
import br.com.fatec.mogi.inventory_service.coreService.repository.ItemRepository;
import br.com.fatec.mogi.inventory_service.coreService.strategy.validarItem.ValidarItemContexto;
import br.com.fatec.mogi.inventory_service.coreService.strategy.validarItem.ValidarItemNavigation;
import br.com.fatec.mogi.inventory_service.coreService.web.request.ItemUploadRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemUploadProcessor implements ItemProcessor<ItemUploadRequestDTO, Item> {

	private final ItemRepository itemRepository;

	private final ValidarItemNavigation validarItemNavigation;

	@Override
	public Item process(ItemUploadRequestDTO item) {
		ValidarItemContexto contexto = new ValidarItemContexto();
		validarItemNavigation.executar(item, contexto);

		if (contexto.isEncerrarFluxo()) {
			return null;
		}

		return contexto.getItem();
	}

}

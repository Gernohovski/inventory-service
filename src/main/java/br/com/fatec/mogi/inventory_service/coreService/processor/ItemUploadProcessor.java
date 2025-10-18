package br.com.fatec.mogi.inventory_service.coreService.processor;

import br.com.fatec.mogi.inventory_service.coreService.domain.model.Item;
import br.com.fatec.mogi.inventory_service.coreService.repository.ItemRepository;
import br.com.fatec.mogi.inventory_service.coreService.strategy.validarItem.ValidarItemContexto;
import br.com.fatec.mogi.inventory_service.coreService.strategy.validarItem.ValidarItemNavigation;
import br.com.fatec.mogi.inventory_service.coreService.web.request.ItemUploadRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemUploadProcessor implements ItemProcessor<ItemUploadRequestDTO, Item> {

	private final ItemRepository itemRepository;

	private final ValidarItemNavigation validarItemNavigation;

	@Override
	public Item process(ItemUploadRequestDTO item) throws Exception {
		log.info("Processando item linha {}: {}", item.getNumeroLinha(), item.getNome());
		ValidarItemContexto contexto = new ValidarItemContexto();
		validarItemNavigation.executar(item, contexto);

		if (contexto.isEncerrarFluxo()) {
			String errosMsg = contexto.getErros()
				.stream()
				.map(erro -> erro.getMensagem())
				.reduce((a, b) -> a + "; " + b)
				.orElse("Erro de validação");
			log.warn("Item linha {} rejeitado. Erros: {}", item.getNumeroLinha(), errosMsg);
			throw new Exception(errosMsg);
		}

		log.info("Item linha {} validado com sucesso: {}", item.getNumeroLinha(), contexto.getItem().getCodigoItem());
		return contexto.getItem();
	}

}

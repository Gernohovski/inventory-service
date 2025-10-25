package br.com.fatec.mogi.inventory_service.coreService.listener;

import br.com.fatec.mogi.inventory_service.coreService.domain.model.Item;
import br.com.fatec.mogi.inventory_service.coreService.web.request.ItemUploadRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.UploadErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ItemUploadSkipListener implements SkipListener<ItemUploadRequestDTO, Item> {

	private final List<UploadErrorResponseDTO> erros = new ArrayList<>();

	@Override
	public void onSkipInRead(Throwable t) {
		log.warn("Erro ao ler linha do CSV: {}", t.getMessage());
		erros.add(new UploadErrorResponseDTO(null, t.getMessage()));
	}

	@Override
	public void onSkipInWrite(Item item, Throwable t) {
		log.warn("Erro ao salvar item {}: {}", item.getCodigoItem(), t.getMessage());
		erros.add(new UploadErrorResponseDTO(item.getCodigoItem(), t.getMessage()));
	}

	@Override
	public void onSkipInProcess(ItemUploadRequestDTO item, Throwable t) {
		log.warn("Erro ao processar linha {}: {}", item.getNumeroLinha(), t.getMessage());
		erros.add(new UploadErrorResponseDTO(item.getNumeroLinha().toString(), t.getMessage()));
	}

	public List<UploadErrorResponseDTO> getErros() {
		return new ArrayList<>(erros);
	}

	public void limparErros() {
		erros.clear();
	}

}

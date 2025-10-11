package br.com.fatec.mogi.inventory_service.coreService.strategy.impl;

import br.com.fatec.mogi.inventory_service.coreService.repository.StatusItemRepository;
import br.com.fatec.mogi.inventory_service.coreService.strategy.ValidarItemContexto;
import br.com.fatec.mogi.inventory_service.coreService.strategy.ValidarItemStrategy;
import br.com.fatec.mogi.inventory_service.coreService.web.request.ItemUploadRequestDTO;
import jakarta.annotation.Priority;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Priority(5)
@Component
@RequiredArgsConstructor
public class ValidarStatusStrategy implements ValidarItemStrategy {

	private final StatusItemRepository statusItemRepository;

	@Override
	public void executar(ItemUploadRequestDTO dto, ValidarItemContexto contexto) {
		contexto.adicionarStrategieExecutada(this);
		if (dto.getCondicao() == null || dto.getCondicao().trim().isEmpty()) {
			contexto.adicionarErro("Condição inválida", dto.getNumeroLinha().toString());
			contexto.setEncerrarFluxo(true);
			return;
		}
		var status = statusItemRepository.findByNome(dto.getCondicao());
		if (status.isEmpty()) {
			contexto.adicionarErro("Condição não mapeada", dto.getNumeroLinha().toString());
			contexto.setEncerrarFluxo(true);
			return;
		}
		contexto.getItem().setStatusItem(status.get());
	}

}

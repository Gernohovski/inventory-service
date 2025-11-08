package br.com.fatec.mogi.inventory_service.coreService.strategy.validarItem.impl;

import br.com.fatec.mogi.inventory_service.coreService.domain.exception.StatusItemNaoEncontradoException;
import br.com.fatec.mogi.inventory_service.coreService.domain.model.StatusItem;
import br.com.fatec.mogi.inventory_service.coreService.repository.StatusItemRepository;
import br.com.fatec.mogi.inventory_service.coreService.strategy.validarItem.ValidarItemContexto;
import br.com.fatec.mogi.inventory_service.coreService.strategy.validarItem.ValidarItemStrategy;
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
			var ativo = statusItemRepository.findByNome("Ativo").orElseThrow(StatusItemNaoEncontradoException::new);
			contexto.getItem().setStatusItem(ativo);
			return;
		}
		var status = statusItemRepository.findByNome(dto.getCondicao());
		if (status.isEmpty()) {
			var novoStatus = statusItemRepository.save(new StatusItem(dto.getCondicao()));
			contexto.getItem().setStatusItem(novoStatus);
			return;
		}
		contexto.getItem().setStatusItem(status.get());
	}

}

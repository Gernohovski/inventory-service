package br.com.fatec.mogi.inventory_service.coreService.strategy.validarItem.impl;

import br.com.fatec.mogi.inventory_service.coreService.repository.ItemRepository;
import br.com.fatec.mogi.inventory_service.coreService.strategy.validarItem.ValidarItemContexto;
import br.com.fatec.mogi.inventory_service.coreService.strategy.validarItem.ValidarItemStrategy;
import br.com.fatec.mogi.inventory_service.coreService.web.request.ItemUploadRequestDTO;
import jakarta.annotation.Priority;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Priority(2)
@Component
@RequiredArgsConstructor
public class ValidarCodigoStrategy implements ValidarItemStrategy {

	private final ItemRepository itemRepository;

	@Override
	public void executar(ItemUploadRequestDTO dto, ValidarItemContexto contexto) {
		contexto.adicionarStrategieExecutada(this);

		if (dto.getCodigo() == null || dto.getCodigo().trim().isEmpty()) {
			contexto.adicionarErro("Código inválido", dto.getNumeroLinha().toString());
			contexto.setEncerrarFluxo(true);
			return;
		}

		if (itemRepository.existsByCodigoItem(dto.getCodigo())) {
			contexto.adicionarErro("Código já cadastrado: " + dto.getCodigo(), dto.getNumeroLinha().toString());
			contexto.setEncerrarFluxo(true);
			return;
		}

		contexto.getItem().setCodigoItem(dto.getCodigo());
	}

}

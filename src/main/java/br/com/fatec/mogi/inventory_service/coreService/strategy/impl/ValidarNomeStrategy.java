package br.com.fatec.mogi.inventory_service.coreService.strategy.impl;

import br.com.fatec.mogi.inventory_service.coreService.strategy.ValidarItemContexto;
import br.com.fatec.mogi.inventory_service.coreService.strategy.ValidarItemStrategy;
import br.com.fatec.mogi.inventory_service.coreService.web.request.ItemUploadRequestDTO;
import jakarta.annotation.Priority;
import org.springframework.stereotype.Component;

@Priority(1)
@Component
public class ValidarNomeStrategy implements ValidarItemStrategy {

	@Override
	public void executar(ItemUploadRequestDTO dto, ValidarItemContexto contexto) {
		contexto.adicionarStrategieExecutada(this);
		if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
			contexto.adicionarErro("Nome inválido", dto.getNumeroLinha().toString());
			contexto.setEncerrarFluxo(true);
		}
		else {
			contexto.getItem().setNomeItem(dto.getNome());
		}
	}

}

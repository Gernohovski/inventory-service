package br.com.fatec.mogi.inventory_service.coreService.strategy.impl;

import br.com.fatec.mogi.inventory_service.coreService.repository.TipoEntradaRepository;
import br.com.fatec.mogi.inventory_service.coreService.strategy.ValidarItemContexto;
import br.com.fatec.mogi.inventory_service.coreService.strategy.ValidarItemStrategy;
import br.com.fatec.mogi.inventory_service.coreService.web.request.ItemUploadRequestDTO;
import jakarta.annotation.Priority;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Priority(6)
@Component
@RequiredArgsConstructor
public class ValidarTipoEntrada implements ValidarItemStrategy {

	private final TipoEntradaRepository tipoEntradaRepository;

	@Override
	public void executar(ItemUploadRequestDTO dto, ValidarItemContexto contexto) {
		contexto.adicionarStrategieExecutada(this);
		if (dto.getTipoEntrada() == null || dto.getTipoEntrada().trim().isEmpty()) {
			contexto.adicionarErro("Tipo entrada inválido", dto.getNumeroLinha().toString());
			contexto.setEncerrarFluxo(true);
			return;
		}
		var tipoEntrada = tipoEntradaRepository.findByNome(dto.getTipoEntrada());
		if (tipoEntrada.isEmpty()) {
			contexto.adicionarErro("Tipo entrada não mapeado", dto.getNumeroLinha().toString());
			contexto.setEncerrarFluxo(true);
			return;
		}
		contexto.getItem().setTipoEntrada(tipoEntrada.get());
	}

}

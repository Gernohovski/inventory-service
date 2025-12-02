package br.com.fatec.mogi.inventory_service.coreService.strategy.validarItem.impl;

import br.com.fatec.mogi.inventory_service.coreService.domain.exception.TipoEntradaNaoEncontradaException;
import br.com.fatec.mogi.inventory_service.coreService.domain.model.TipoEntrada;
import br.com.fatec.mogi.inventory_service.coreService.repository.TipoEntradaRepository;
import br.com.fatec.mogi.inventory_service.coreService.strategy.validarItem.ValidarItemContexto;
import br.com.fatec.mogi.inventory_service.coreService.strategy.validarItem.ValidarItemStrategy;
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
			var compra = tipoEntradaRepository.findByNome("Compra").orElseThrow(TipoEntradaNaoEncontradaException::new);
			contexto.getItem().setTipoEntrada(compra);
			return;
		}
		var tipoEntrada = tipoEntradaRepository.findByNome(dto.getTipoEntrada());
		if (tipoEntrada.isEmpty()) {
			TipoEntrada tipoEntradaNovo = TipoEntrada.builder().nome(dto.getTipoEntrada()).build();
			var tipoEntradaSalvo = tipoEntradaRepository.save(tipoEntradaNovo);
			contexto.getItem().setTipoEntrada(tipoEntradaSalvo);
			return;
		}
		contexto.getItem().setTipoEntrada(tipoEntrada.get());
	}

}

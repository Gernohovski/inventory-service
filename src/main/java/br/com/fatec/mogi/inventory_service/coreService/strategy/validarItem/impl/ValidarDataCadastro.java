package br.com.fatec.mogi.inventory_service.coreService.strategy.validarItem.impl;

import br.com.fatec.mogi.inventory_service.coreService.strategy.validarItem.ValidarItemContexto;
import br.com.fatec.mogi.inventory_service.coreService.strategy.validarItem.ValidarItemStrategy;
import br.com.fatec.mogi.inventory_service.coreService.web.request.ItemUploadRequestDTO;
import jakarta.annotation.Priority;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Priority(7)
@Component
@RequiredArgsConstructor
public class ValidarDataCadastro implements ValidarItemStrategy {

	@Override
	public void executar(ItemUploadRequestDTO dto, ValidarItemContexto contexto) {
		contexto.adicionarStrategieExecutada(this);

		if (dto.getDataCadastro() == null || dto.getDataCadastro().trim().isEmpty()) {
			LocalDateTime agora = LocalDateTime.now();
			contexto.getItem().setDataCadastro(agora);
			contexto.getItem().setDataAlteracao(agora);
			return;
		}

		try {
			LocalDateTime dataParsed = LocalDate.parse(dto.getDataCadastro()).atStartOfDay();
			contexto.getItem().setDataCadastro(dataParsed);
			contexto.getItem().setDataAlteracao(dataParsed);
		}
		catch (Exception e) {
			contexto.adicionarErro("Data de cadastro inválida: " + dto.getDataCadastro(),
					dto.getNumeroLinha().toString());
			contexto.setEncerrarFluxo(true);
		}
	}

}

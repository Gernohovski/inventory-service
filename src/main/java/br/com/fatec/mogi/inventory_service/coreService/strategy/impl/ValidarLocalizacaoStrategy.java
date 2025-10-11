package br.com.fatec.mogi.inventory_service.coreService.strategy.impl;

import br.com.fatec.mogi.inventory_service.coreService.repository.LocalizacaoRepository;
import br.com.fatec.mogi.inventory_service.coreService.strategy.ValidarItemContexto;
import br.com.fatec.mogi.inventory_service.coreService.strategy.ValidarItemStrategy;
import br.com.fatec.mogi.inventory_service.coreService.web.request.ItemUploadRequestDTO;
import jakarta.annotation.Priority;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Priority(3)
@Component
@RequiredArgsConstructor
public class ValidarLocalizacaoStrategy implements ValidarItemStrategy {

	private final LocalizacaoRepository localizacaoRepository;

	@Override
	public void executar(ItemUploadRequestDTO dto, ValidarItemContexto contexto) {
		contexto.adicionarStrategieExecutada(this);
		if (dto.getLocalizacao() == null || dto.getLocalizacao().trim().isEmpty()) {
			contexto.adicionarErro("Nome da sala inválido", dto.getNumeroLinha().toString());
			contexto.setEncerrarFluxo(true);
			return;
		}
		var localizacao = localizacaoRepository.findByNomeSala(dto.getLocalizacao());
		if (localizacao.isEmpty()) {
			contexto.adicionarErro("Localização não mapeada", dto.getLocalizacao());
			contexto.setEncerrarFluxo(true);
			return;
		}
		contexto.getItem().setLocalizacao(localizacao.get());
	}

}

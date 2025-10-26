package br.com.fatec.mogi.inventory_service.coreService.strategy.validarItem.impl;

import br.com.fatec.mogi.inventory_service.coreService.domain.model.Localizacao;
import br.com.fatec.mogi.inventory_service.coreService.repository.LocalizacaoRepository;
import br.com.fatec.mogi.inventory_service.coreService.strategy.validarItem.ValidarItemContexto;
import br.com.fatec.mogi.inventory_service.coreService.strategy.validarItem.ValidarItemStrategy;
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
			Localizacao novaLocalizacao = Localizacao.builder().nomeSala(dto.getLocalizacao()).build();
			var localizacaoSalva = localizacaoRepository.save(novaLocalizacao);
			contexto.getItem().setLocalizacao(localizacaoSalva);
			return;
		}
		contexto.getItem().setLocalizacao(localizacao.get());
	}

}

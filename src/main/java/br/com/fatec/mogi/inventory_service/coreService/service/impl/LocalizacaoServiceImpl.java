package br.com.fatec.mogi.inventory_service.coreService.service.impl;

import br.com.fatec.mogi.inventory_service.coreService.domain.model.Localizacao;
import br.com.fatec.mogi.inventory_service.coreService.repository.LocalizacaoRepository;
import br.com.fatec.mogi.inventory_service.coreService.service.LocalizacaoService;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarLocalizacaoRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.BuscarLocalizacaoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocalizacaoServiceImpl implements LocalizacaoService {

	private final LocalizacaoRepository localizacaoRepository;

	@Override
	public BuscarLocalizacaoResponseDTO buscar() {
		return new BuscarLocalizacaoResponseDTO(localizacaoRepository.findAll());
	}

	@Override
	public void cadastrar(CadastrarLocalizacaoRequestDTO dto) {
		Localizacao localizacao = Localizacao.builder().andar(dto.getAndar()).nomeSala(dto.getNomeSala()).build();
		localizacaoRepository.save(localizacao);
	}

}

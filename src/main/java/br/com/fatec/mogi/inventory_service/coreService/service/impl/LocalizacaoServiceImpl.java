package br.com.fatec.mogi.inventory_service.coreService.service.impl;

import br.com.fatec.mogi.inventory_service.common.web.response.CustomPageResponseDTO;
import br.com.fatec.mogi.inventory_service.coreService.domain.model.Localizacao;
import br.com.fatec.mogi.inventory_service.coreService.repository.LocalizacaoRepository;
import br.com.fatec.mogi.inventory_service.coreService.service.LocalizacaoService;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarLocalizacaoRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.BuscarLocalizacaoResponseDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.LocalizacaoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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

	@Override
	public CustomPageResponseDTO<LocalizacaoResponseDTO> buscarPaginado(Pageable pageable) {
		var localizacoes = localizacaoRepository.findPaginado(pageable);
		return CustomPageResponseDTO.<LocalizacaoResponseDTO>builder()
			.page(localizacoes.getNumber())
			.size(localizacoes.getSize())
			.totalElements(localizacoes.getTotalElements())
			.totalPages(localizacoes.getTotalPages())
			.content(localizacoes.getContent())
			.build();
	}

}

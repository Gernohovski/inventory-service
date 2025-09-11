package br.com.fatec.mogi.inventory_service.coreService.service.impl;

import br.com.fatec.mogi.inventory_service.authService.service.AutorizacaoService;
import br.com.fatec.mogi.inventory_service.authService.web.dto.request.AutorizarUsuarioRequestDTO;
import br.com.fatec.mogi.inventory_service.common.domain.enums.FuncionalidadesEnum;
import br.com.fatec.mogi.inventory_service.coreService.repository.LocalizacaoRepository;
import br.com.fatec.mogi.inventory_service.coreService.service.LocalizacaoService;
import br.com.fatec.mogi.inventory_service.coreService.web.response.BuscarLocalizacaoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocalizacaoServiceImpl implements LocalizacaoService {

	private final LocalizacaoRepository localizacaoRepository;

	private final AutorizacaoService autorizacaoService;

	@Override
	public BuscarLocalizacaoResponseDTO buscar(String accessToken) {
		AutorizarUsuarioRequestDTO autorizarUsuarioRequestDTO = AutorizarUsuarioRequestDTO.builder()
			.funcionalidade(FuncionalidadesEnum.LISTAR_LOCALIZCAO.toString())
			.build();
		autorizacaoService.autorizar(autorizarUsuarioRequestDTO, accessToken);
		return new BuscarLocalizacaoResponseDTO(localizacaoRepository.findAll());
	}

}

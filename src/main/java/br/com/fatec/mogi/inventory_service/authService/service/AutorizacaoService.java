package br.com.fatec.mogi.inventory_service.authService.service;

import br.com.fatec.mogi.inventory_service.authService.web.dto.request.AutorizarUsuarioRequestDTO;

public interface AutorizacaoService {

	void autorizar(AutorizarUsuarioRequestDTO dto, String accessToken);

}

package br.com.fatec.mogi.inventory_service.authService.service;

import br.com.fatec.mogi.inventory_service.authService.web.dto.request.AutorizarUsuarioRequestDTO;

public interface AutorizacaoService {

	boolean autorizar(AutorizarUsuarioRequestDTO dto, String accessToken);

}

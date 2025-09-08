package br.com.fatec.mogi.inventory_service.authService.service;

import br.com.fatec.mogi.inventory_service.authService.domain.model.Usuario;
import br.com.fatec.mogi.inventory_service.authService.web.dto.request.LogoutRequestDTO;
import br.com.fatec.mogi.inventory_service.authService.web.dto.request.RefreshTokenRequestDTO;
import br.com.fatec.mogi.inventory_service.authService.web.dto.response.LoginResponseDTO;
import br.com.fatec.mogi.inventory_service.authService.web.dto.response.RefreshTokenResponseDTO;
import com.auth0.jwt.interfaces.DecodedJWT;

public interface AutenticacaoService {

	LoginResponseDTO gerarAutenticacao(Usuario usuario);

	RefreshTokenResponseDTO gerarAutenticacao(RefreshTokenRequestDTO dto);

	DecodedJWT decodeJwt(String jwt);

	void logout(LogoutRequestDTO logoutRequestDTO);

}

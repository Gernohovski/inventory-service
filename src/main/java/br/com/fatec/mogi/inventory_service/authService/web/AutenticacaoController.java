package br.com.fatec.mogi.inventory_service.authService.web;

import br.com.fatec.mogi.inventory_service.authService.service.AutenticacaoService;
import br.com.fatec.mogi.inventory_service.authService.service.UsuarioService;
import br.com.fatec.mogi.inventory_service.authService.web.dto.request.LoginRequestDTO;
import br.com.fatec.mogi.inventory_service.authService.web.dto.request.LogoutRequestDTO;
import br.com.fatec.mogi.inventory_service.authService.web.dto.request.RefreshTokenRequestDTO;
import br.com.fatec.mogi.inventory_service.authService.web.dto.response.LoginResponseDTO;
import br.com.fatec.mogi.inventory_service.authService.web.dto.response.RefreshTokenResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth-service/v1/autenticacao")
public record AutenticacaoController(UsuarioService usuarioService, AutenticacaoService autenticacaoService) {

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
		var tokensDto = usuarioService.login(dto);
		return ResponseEntity.status(HttpStatus.OK).body(tokensDto);
	}

	@PutMapping("/refresh")
	public ResponseEntity<RefreshTokenResponseDTO> refresh(@RequestBody RefreshTokenRequestDTO dto) {
		var tokensDto = autenticacaoService.gerarAutenticacao(dto);
		return ResponseEntity.status(HttpStatus.OK).body(tokensDto);
	}

	@PutMapping("/logout")
	public ResponseEntity<?> logout(@RequestHeader("X-REFRESH-TOKEN") String refreshToken,
			@RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		autenticacaoService.logout(new LogoutRequestDTO(accessToken, refreshToken));
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}

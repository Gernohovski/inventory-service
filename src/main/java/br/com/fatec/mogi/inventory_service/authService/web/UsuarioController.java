package br.com.fatec.mogi.inventory_service.authService.web;

import br.com.fatec.mogi.inventory_service.authService.service.UsuarioService;
import br.com.fatec.mogi.inventory_service.authService.web.dto.request.AlterarSenhaRequestDTO;
import br.com.fatec.mogi.inventory_service.authService.web.dto.request.CadastrarUsuarioRequestDTO;
import br.com.fatec.mogi.inventory_service.authService.web.dto.request.SolicitarResetSenhaRequestDTO;
import br.com.fatec.mogi.inventory_service.authService.web.dto.response.CadastrarUsuarioResponseDTO;
import br.com.fatec.mogi.inventory_service.authService.web.dto.response.SolicitarResetSenhaResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth-service/v1/usuarios")
public record UsuarioController(UsuarioService usuarioService) {

	@PostMapping
	public ResponseEntity<CadastrarUsuarioResponseDTO> cadastrarUsuario(@RequestBody CadastrarUsuarioRequestDTO dto,
			@RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		var usuario = usuarioService.cadastrarUsuario(dto);
		CadastrarUsuarioResponseDTO responseDTO = CadastrarUsuarioResponseDTO.builder()
			.email(usuario.getEmail().getEmail())
			.nome(usuario.getNome())
			.build();
		return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
	}

	@PutMapping("/solicitar-redefinicao-senha")
	public ResponseEntity<SolicitarResetSenhaResponseDTO> solicitarResetSenha(
			@RequestBody SolicitarResetSenhaRequestDTO dto) {
		var emailEnviado = usuarioService.solicitarResetSenha(dto);
		return ResponseEntity.status(HttpStatus.OK).body(new SolicitarResetSenhaResponseDTO(emailEnviado));
	}

	@PutMapping("/alterar-senha")
	public ResponseEntity<?> alterarSenha(@RequestBody AlterarSenhaRequestDTO dto) {
		usuarioService.alterarSenha(dto);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
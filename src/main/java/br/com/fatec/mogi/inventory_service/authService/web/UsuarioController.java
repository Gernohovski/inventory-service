package br.com.fatec.mogi.inventory_service.authService.web;

import br.com.fatec.mogi.inventory_service.authService.service.UsuarioService;
import br.com.fatec.mogi.inventory_service.authService.web.dto.request.*;
import br.com.fatec.mogi.inventory_service.authService.web.dto.response.CadastrarUsuarioResponseDTO;
import br.com.fatec.mogi.inventory_service.authService.web.dto.response.SolicitarResetSenhaResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@GetMapping
	public ResponseEntity<?> listarUsuarios(@RequestHeader("X-ACCESS-TOKEN") String accessToken,
			@PageableDefault Pageable pageable, @ModelAttribute ConsultarUsuarioRequestDTO dto) {
		var usuarios = usuarioService.listarUsuarios(pageable, dto);
		return ResponseEntity.status(HttpStatus.OK).body(usuarios);
	}

	@GetMapping("/administradores")
	public ResponseEntity<?> listarAdministradores(@RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		var administradores = usuarioService.listarAdministradores();
		return ResponseEntity.status(HttpStatus.OK).body(administradores);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletarUsuario(@PathVariable("id") Long id,
			@RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		usuarioService.deletarUsuario(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> atualizarUsuario(@PathVariable("id") Long id,
			@RequestHeader("X-ACCESS-TOKEN") String accessToken, @RequestBody AtualizarUsuarioRequestDTO dto) {
		var usuarioAtualizado = usuarioService.atualizarUsuario(id, dto);
		return ResponseEntity.status(HttpStatus.OK).body(usuarioAtualizado);
	}

	@PutMapping("/desativar-auditoria/{id}")
	public ResponseEntity<?> desativarAuditoria(@PathVariable("id") Long id,
			@RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		usuarioService.desativarAuditoria(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PutMapping("/ativar-auditoria/{id}")
	public ResponseEntity<?> ativarAuditoria(@PathVariable("id") Long id,
			@RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		usuarioService.ativarAuditoria(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PutMapping("/desativar-auditoria/todos")
	public ResponseEntity<?> desativarAuditoriaTodos(@RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		usuarioService.desativarAuditoriaTodos();
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PutMapping("/ativar-auditoria/todos")
	public ResponseEntity<?> ativarAuditoriaTodos(@RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		usuarioService.ativarAuditoriaTodos();
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
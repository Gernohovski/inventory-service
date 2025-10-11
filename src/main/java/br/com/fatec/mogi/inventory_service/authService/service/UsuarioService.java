package br.com.fatec.mogi.inventory_service.authService.service;

import br.com.fatec.mogi.inventory_service.authService.domain.model.Usuario;
import br.com.fatec.mogi.inventory_service.authService.web.dto.request.*;
import br.com.fatec.mogi.inventory_service.authService.web.dto.response.LoginResponseDTO;
import br.com.fatec.mogi.inventory_service.authService.web.dto.response.UsuarioResponseDTO;
import br.com.fatec.mogi.inventory_service.common.web.response.CustomPageResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UsuarioService {

	Usuario cadastrarUsuario(CadastrarUsuarioRequestDTO dto);

	LoginResponseDTO login(LoginRequestDTO dto);

	boolean solicitarResetSenha(SolicitarResetSenhaRequestDTO dto);

	void alterarSenha(AlterarSenhaRequestDTO dto);

	CustomPageResponseDTO<UsuarioResponseDTO> listarUsuarios(Pageable pageable);

	List<UsuarioResponseDTO> listarAdministradores();

	void deletarUsuario(Long id);

	UsuarioResponseDTO atualizarUsuario(Long id, AtualizarUsuarioRequestDTO dto);

}
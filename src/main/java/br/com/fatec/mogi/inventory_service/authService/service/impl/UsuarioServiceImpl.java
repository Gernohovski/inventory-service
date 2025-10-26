package br.com.fatec.mogi.inventory_service.authService.service.impl;

import br.com.fatec.mogi.inventory_service.authService.domain.enums.TipoCache;
import br.com.fatec.mogi.inventory_service.authService.domain.exception.*;
import br.com.fatec.mogi.inventory_service.authService.domain.model.Funcao;
import br.com.fatec.mogi.inventory_service.authService.domain.model.Usuario;
import br.com.fatec.mogi.inventory_service.authService.domain.model.UsuarioFuncao;
import br.com.fatec.mogi.inventory_service.authService.domain.model.valueObjects.Email;
import br.com.fatec.mogi.inventory_service.authService.domain.model.valueObjects.Senha;
import br.com.fatec.mogi.inventory_service.authService.repository.FuncaoRepository;
import br.com.fatec.mogi.inventory_service.authService.repository.UsuarioFuncaoRepository;
import br.com.fatec.mogi.inventory_service.authService.repository.UsuarioRepository;
import br.com.fatec.mogi.inventory_service.authService.service.AutenticacaoService;
import br.com.fatec.mogi.inventory_service.authService.service.EmailService;
import br.com.fatec.mogi.inventory_service.authService.service.RedisService;
import br.com.fatec.mogi.inventory_service.authService.service.UsuarioService;
import br.com.fatec.mogi.inventory_service.authService.web.dto.mapper.UsuarioResponseDTOMapper;
import br.com.fatec.mogi.inventory_service.authService.web.dto.request.*;
import br.com.fatec.mogi.inventory_service.authService.web.dto.response.LoginResponseDTO;
import br.com.fatec.mogi.inventory_service.authService.web.dto.response.UsuarioResponseDTO;
import br.com.fatec.mogi.inventory_service.common.web.response.CustomPageResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

	private final EmailService emailService;

	private final AutenticacaoService autenticacaoService;

	private final UsuarioRepository usuarioRepository;

	private final FuncaoRepository funcaoRepository;

	private final UsuarioFuncaoRepository usuarioFuncaoRepository;

	private final RedisService redisService;

	private final UsuarioResponseDTOMapper usuarioResponseDTOMapper;

	@Override
	@Transactional
	public Usuario cadastrarUsuario(CadastrarUsuarioRequestDTO dto) {
		Funcao funcao = funcaoRepository.findById(dto.getFuncaoId())
			.orElseThrow(() -> new FuncaoNaoEncontrada("Função não encontrada"));
		Usuario usuario;
		Usuario administradorVinculado = null;
		if (dto.getAdministradorVinculado() != null) {
			administradorVinculado = usuarioRepository.findById(dto.getAdministradorVinculado())
				.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
		}
		usuario = new Usuario(dto.getNome(), dto.getSenha(), dto.getEmail(), administradorVinculado);
		usuarioRepository.findByEmail(usuario.getEmail()).ifPresent(u -> {
			throw new EmailJaUtilizadoException("E-mail já utilizado.");
		});
		usuario.setDataCriacao(LocalDateTime.now());
		usuario.setDataAlteracao(LocalDateTime.now());
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		usuarioFuncaoRepository.save(new UsuarioFuncao(usuarioSalvo, funcao));
		return usuarioSalvo;
	}

	@Override
	public LoginResponseDTO login(LoginRequestDTO dto) {
		var usuario = usuarioRepository.findByEmail(new Email(dto.getEmail())).orElseThrow(LoginInvalidoException::new);
		if (!BCrypt.checkpw(dto.getSenha(), usuario.getSenha().getSenha())) {
			throw new LoginInvalidoException();
		}
		return autenticacaoService.gerarAutenticacao(usuario);
	}

	@Override
	public boolean solicitarResetSenha(SolicitarResetSenhaRequestDTO dto) {
		var usuario = usuarioRepository.findByEmail(new Email(dto.getEmail()))
			.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado."));
		var administradorVinculado = usuario.getAdministradorVinculado();
		return emailService.enviarEmailResetSenha(usuario, Objects.requireNonNullElse(administradorVinculado, usuario));
	}

	@Override
	@Transactional
	public void alterarSenha(AlterarSenhaRequestDTO dto) {
		var usuario = (Usuario) redisService.buscar(TipoCache.CODIGO_RESET_SENHA, dto.getCodigo());
		Optional.ofNullable(usuario).orElseThrow(SolicitacaoExpiradaExpcetion::new);
		if (!usuario.getEmail().getEmail().equals(dto.getEmail())) {
			throw new UsuariosDivergentesException();
		}
		usuario.setSenha(new Senha(dto.getNovaSenha()));
		usuarioRepository.save(usuario);
	}

	@Override
	public CustomPageResponseDTO<UsuarioResponseDTO> listarUsuarios(Pageable pageable, ConsultarUsuarioRequestDTO dto) {
		var usuarios = usuarioRepository.findAllUsuarios(pageable, dto);
		return CustomPageResponseDTO.<UsuarioResponseDTO>builder()
			.page(usuarios.getNumber())
			.size(usuarios.getSize())
			.totalElements(usuarios.getTotalElements())
			.totalPages(usuarios.getTotalPages())
			.content(usuarios.getContent())
			.build();
	}

	@Override
	public List<UsuarioResponseDTO> listarAdministradores() {
		return usuarioRepository.findUsuariosAdministradores();
	}

	@Override
	@Transactional
	public void deletarUsuario(Long id) {
		var usuario = usuarioRepository.findById(id)
			.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado."));
		usuario.setAtivo(false);
		usuarioRepository.save(usuario);
	}

	@Override
	@Transactional
	public UsuarioResponseDTO atualizarUsuario(Long id, AtualizarUsuarioRequestDTO dto) {
		var usuario = usuarioRepository.findById(id)
			.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado."));
		if (dto.getEmail() != null) {
			Email novoEmail = new Email(dto.getEmail());
			usuarioRepository.findByEmail(novoEmail).ifPresent(u -> {
				if (!u.getId().equals(id)) {
					throw new EmailJaUtilizadoException("E-mail já utilizado.");
				}
			});
			usuario.setEmail(novoEmail);
		}
		if (dto.getSenha() != null) {
			usuario.setSenha(new Senha(dto.getSenha()));
		}
		if (dto.getFuncaoId() != null) {
			usuarioFuncaoRepository.deleteByUsuarioId(id);
			var novaFuncao = funcaoRepository.findById(dto.getFuncaoId())
				.orElseThrow(() -> new FuncaoNaoEncontrada("Função não encontrada"));
			UsuarioFuncao novoUsuarioFuncao = UsuarioFuncao.builder().usuario(usuario).funcao(novaFuncao).build();
			usuarioFuncaoRepository.save(novoUsuarioFuncao);
		}
		if (dto.getNome() != null) {
			usuario.setNome(dto.getNome());
		}
		usuario.setAtivo(dto.isAtivo());
		usuario.setDataAlteracao(LocalDateTime.now());
		var usuarioAtualizado = usuarioRepository.save(usuario);
		return usuarioResponseDTOMapper.from(usuarioAtualizado);
	}

	@Override
	public void desativarAuditoria(Long id) {
		var usuario = usuarioRepository.findById(id)
			.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado."));
		usuario.setPodeRealizarAuditoria(false);
		usuarioRepository.save(usuario);
	}

	@Override
	public void ativarAuditoria(Long id) {
		var usuario = usuarioRepository.findById(id)
			.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado."));
		usuario.setPodeRealizarAuditoria(true);
		usuarioRepository.save(usuario);
	}

	@Override
	@Transactional
	public void desativarAuditoriaTodos() {
		usuarioRepository.updatePodeRealizarAuditoria(false);
	}

	@Override
	@Transactional
	public void ativarAuditoriaTodos() {
		usuarioRepository.updatePodeRealizarAuditoria(true);
	}

}

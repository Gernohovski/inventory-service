package br.com.fatec.mogi.inventory_service.authService.service.impl;

import br.com.fatec.mogi.inventory_service.authService.domain.enums.TipoCache;
import br.com.fatec.mogi.inventory_service.authService.domain.exception.EmailJaUtilizadoException;
import br.com.fatec.mogi.inventory_service.authService.domain.exception.FuncaoNaoEncontrada;
import br.com.fatec.mogi.inventory_service.authService.domain.exception.LoginInvalidoException;
import br.com.fatec.mogi.inventory_service.authService.domain.exception.SolicitacaoExpiradaExpcetion;
import br.com.fatec.mogi.inventory_service.authService.domain.exception.UsuarioNaoEncontradoException;
import br.com.fatec.mogi.inventory_service.authService.domain.exception.UsuariosDivergentesException;
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
import br.com.fatec.mogi.inventory_service.authService.web.dto.request.AlterarSenhaRequestDTO;
import br.com.fatec.mogi.inventory_service.authService.web.dto.request.CadastrarUsuarioRequestDTO;
import br.com.fatec.mogi.inventory_service.authService.web.dto.request.LoginRequestDTO;
import br.com.fatec.mogi.inventory_service.authService.web.dto.request.SolicitarResetSenhaRequestDTO;
import br.com.fatec.mogi.inventory_service.authService.web.dto.response.LoginResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

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

	@Override
	@Transactional
	public Usuario cadastrarUsuario(CadastrarUsuarioRequestDTO dto) {
		Funcao funcao = funcaoRepository.findById(dto.getFuncaoId())
			.orElseThrow(() -> new FuncaoNaoEncontrada("Função não encontrada"));
		Usuario usuario;
		Usuario administradorVinculado = usuarioRepository.findById(dto.getAdministradorVinculado())
			.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
		usuario = new Usuario(dto.getNome(), dto.getSenha(), dto.getEmail(), administradorVinculado);
		usuarioRepository.findByEmail(usuario.getEmail()).ifPresent(u -> {
			throw new EmailJaUtilizadoException("E-mail já utilizado.");
		});
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

}

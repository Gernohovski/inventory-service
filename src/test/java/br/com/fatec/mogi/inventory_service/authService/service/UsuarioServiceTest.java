package br.com.fatec.mogi.inventory_service.authService.service;

import br.com.fatec.mogi.inventory_service.authService.domain.enums.TipoCache;
import br.com.fatec.mogi.inventory_service.authService.domain.exception.*;
import br.com.fatec.mogi.inventory_service.authService.domain.model.Usuario;
import br.com.fatec.mogi.inventory_service.authService.domain.model.valueObjects.Email;
import br.com.fatec.mogi.inventory_service.authService.repository.FuncaoRepository;
import br.com.fatec.mogi.inventory_service.authService.repository.UsuarioFuncaoRepository;
import br.com.fatec.mogi.inventory_service.authService.repository.UsuarioRepository;
import br.com.fatec.mogi.inventory_service.authService.utils.GeradorCodigo;
import br.com.fatec.mogi.inventory_service.authService.web.dto.request.AlterarSenhaRequestDTO;
import br.com.fatec.mogi.inventory_service.authService.web.dto.request.CadastrarUsuarioRequestDTO;
import br.com.fatec.mogi.inventory_service.authService.web.dto.request.LoginRequestDTO;
import br.com.fatec.mogi.inventory_service.authService.web.dto.request.SolicitarResetSenhaRequestDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class UsuarioServiceTest {

	@MockitoBean
	private GeradorCodigo geradorCodigo;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private FuncaoRepository funcaoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private UsuarioFuncaoRepository usuarioFuncaoRepository;

	@Autowired
	private RedisService redisService;

	@AfterEach
	void cleanUp() {
		usuarioRepository.deleteAll();
	}

	@Test
	@DisplayName("Deve cadastrar um usuário com sucesso")
	void deveCadastrarUmUsuarioComSucesso() {
		var funcao = funcaoRepository.findAll();
		CadastrarUsuarioRequestDTO dto = CadastrarUsuarioRequestDTO.builder()
			.nome("Usuario teste")
			.email("email@gmail.com")
			.senha("Senha123")
			.funcaoId(funcao.getFirst().getId())
			.build();
		var usuario = usuarioService.cadastrarUsuario(dto);
		assertNotNull(usuario);
		assertEquals("email@gmail.com", usuario.getEmail().getEmail());
		assertTrue(BCrypt.checkpw("Senha123", usuario.getSenha().getSenha()));
		var usuarioFuncao = usuarioFuncaoRepository.findByUsuarioId(usuario.getId());
		assertEquals(funcao.getFirst().getId(), usuarioFuncao.getFirst().getFuncao().getId());
	}

	@ParameterizedTest
	@ValueSource(strings = { "", "teste", "teste@", "@dominio.com" })
	@DisplayName("Não deve cadastrar usuário com e-mail inválido")
	void naoDeveCadastrarUsuarioComEmailInvalido(String email) {
		var funcao = funcaoRepository.findAll();
		CadastrarUsuarioRequestDTO dto = CadastrarUsuarioRequestDTO.builder()
			.nome("Usuario teste")
			.email(email)
			.senha("Senha123")
			.funcaoId(funcao.getFirst().getId())
			.build();
		assertThrows(EmailInvalidoException.class, () -> {
			usuarioService.cadastrarUsuario(dto);
		});
	}

	@ParameterizedTest
	@ValueSource(strings = { "", "senha123", "SENHA123", "asdasdasd" })
	@DisplayName("Não deve cadastrar usuário com senha inválida")
	void naoDeveCadastrarUsuarioSenhaInvalida(String senha) {
		var funcao = funcaoRepository.findAll();
		CadastrarUsuarioRequestDTO dto = CadastrarUsuarioRequestDTO.builder()
			.nome("Usuario teste")
			.email("email@gmail.com")
			.senha(senha)
			.funcaoId(funcao.getFirst().getId())
			.build();
		assertThrows(SenhaInvalidaException.class, () -> {
			usuarioService.cadastrarUsuario(dto);
		});
	}

	@Test
	@DisplayName("Não deve cadastrar usuário com e-mail já cadastrado")
	void naoDeveCadastrarUsuarioComEmailJaCadastrado() {
		var funcao = funcaoRepository.findAll();
		CadastrarUsuarioRequestDTO dto = CadastrarUsuarioRequestDTO.builder()
			.nome("Usuario teste")
			.email("email@gmail.com")
			.senha("Senha123")
			.funcaoId(funcao.getFirst().getId())
			.build();
		var usuario = usuarioService.cadastrarUsuario(dto);
		assertNotNull(usuario);
		assertThrows(EmailJaUtilizadoException.class, () -> {
			usuarioService.cadastrarUsuario(dto);
		});
	}

	@Test
	@DisplayName("Não deve cadastrar usuário para função inválida")
	void naoDeveCadastrarUsuarioParaFuncaoInvalida() {
		CadastrarUsuarioRequestDTO dto = CadastrarUsuarioRequestDTO.builder()
			.nome("Usuario teste")
			.email("email@gmail.com")
			.senha("Senha123")
			.funcaoId(20L)
			.build();
		assertThrows(FuncaoNaoEncontrada.class, () -> {
			usuarioService.cadastrarUsuario(dto);
		});
	}

	@Test
	@DisplayName("Deve realizar o login de um usuário com sucesso")
	void deveRealizarLoginUsuarioComSucesso() {
		String email = "email@gmail.com";
		String senha = "Senha123";
		cadastrarUsuario(email, senha);
		LoginRequestDTO dto = LoginRequestDTO.builder().email(email).senha(senha).build();
		var tokens = usuarioService.login(dto);
		assertNotNull(tokens);
		assertNotNull(tokens.getAccessToken());
		assertNotNull(tokens.getRefreshToken());
		assertEquals(900, tokens.getExpiresIn());
	}

	@Test
	@DisplayName("Não deve realizar login para e-mail inválido")
	void naoDeveRealizarLoginParaEmailInvalido() {
		LoginRequestDTO dto = LoginRequestDTO.builder().email("email@gmail.com").senha("senha").build();
		assertThrows(LoginInvalidoException.class, () -> {
			usuarioService.login(dto);
		});
	}

	@Test
	@DisplayName("Não deve realizar login para senha inválida")
	void naoDeveRealizarLoginParaSenhaInvalida() {
		String email = "email@gmail.com";
		String senha = "Senha123";
		cadastrarUsuario(email, senha);
		LoginRequestDTO dto = LoginRequestDTO.builder().email(email).senha("senha").build();
		assertThrows(LoginInvalidoException.class, () -> {
			usuarioService.login(dto);
		});
	}

	@Test
	@DisplayName("Deve solicitar a redefinição de senha com sucesso")
	void deveSolicitarRedefinicaoSenhaComSucesso() {
		String codigoEsperado = "123456";
		String email = "email@gmail.com";
		String senha = "Senha123";
		cadastrarUsuario(email, senha);
		SolicitarResetSenhaRequestDTO dto = SolicitarResetSenhaRequestDTO.builder().email(email).build();
		when(geradorCodigo.gerarCodigo()).thenReturn(codigoEsperado);
		var emailEnviado = usuarioService.solicitarResetSenha(dto);
		assertTrue(emailEnviado);
		var usuario = (Usuario) redisService.buscar(TipoCache.CODIGO_RESET_SENHA, codigoEsperado);
		assertNotNull(usuario);
	}

	@Test
	@DisplayName("Deve retornar exceção ao solicitar a redefinição de senha para usuário inválido")
	void deveRetornarExcecaoSoliticarRedefinicaoSenhaUsuarioInvalido() {
		SolicitarResetSenhaRequestDTO dto = SolicitarResetSenhaRequestDTO.builder()
			.email(UUID.randomUUID().toString().concat("@gmail.com"))
			.build();
		assertThrows(UsuarioNaoEncontradoException.class, () -> {
			usuarioService.solicitarResetSenha(dto);
		});
	}

	@Test
	@DisplayName("Deve redefinir a senha de um usuário com sucesso")
	void deveRedefinirSenhUsuarioComSucesso() {
		String codigoEsperado = UUID.randomUUID().toString();
		String email = "email@gmail.com";
		String senha = "Senha123";
		String novaSenha = "Senha123456";
		cadastrarUsuario(email, senha);
		SolicitarResetSenhaRequestDTO solicitarResetSenhaDto = SolicitarResetSenhaRequestDTO.builder()
			.email(email)
			.build();
		when(geradorCodigo.gerarCodigo()).thenReturn(codigoEsperado);
		var emailEnviado = usuarioService.solicitarResetSenha(solicitarResetSenhaDto);
		assertTrue(emailEnviado);
		AlterarSenhaRequestDTO alterarSenhaRequestDTO = AlterarSenhaRequestDTO.builder()
			.codigo(codigoEsperado)
			.novaSenha(novaSenha)
			.build();
		usuarioService.alterarSenha(alterarSenhaRequestDTO);
		var usuario = usuarioRepository.findByEmail(new Email(email))
			.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
		assertTrue(BCrypt.checkpw(novaSenha, usuario.getSenha().getSenha()));
	}

	@Test
	@DisplayName("Deve lançar exceção ao tentar alterar senha usuário informando e-mail divergente")
	void deveLancarExcecaoTentarAlterarSenhaUsuarioInformandoEmailDivergente() {
		String codigoEsperado = UUID.randomUUID().toString();
		String email = "email@gmail.com";
		String senha = "Senha123";
		String novaSenha = "Senha123456";
		cadastrarUsuario(email, senha);
		SolicitarResetSenhaRequestDTO solicitarResetSenhaDto = SolicitarResetSenhaRequestDTO.builder()
			.email(email)
			.build();
		when(geradorCodigo.gerarCodigo()).thenReturn(codigoEsperado);
		var emailEnviado = usuarioService.solicitarResetSenha(solicitarResetSenhaDto);
		assertTrue(emailEnviado);
		AlterarSenhaRequestDTO alterarSenhaRequestDTO = AlterarSenhaRequestDTO.builder()
			.codigo(codigoEsperado)
			.novaSenha(novaSenha)
			.build();
		assertThrows(UsuariosDivergentesException.class, () -> {
			usuarioService.alterarSenha(alterarSenhaRequestDTO);
		});
	}

	@Test
	@DisplayName("Deve lançar exceção ao tentar alterar senha usuário informando senha inválida")
	void deveLancarExcecaoTentarAlterarSenhaUsuarioInformandoSenhaInvalida() {
		String codigoEsperado = UUID.randomUUID().toString();
		String email = "email@gmail.com";
		String senha = "Senha123";
		String novaSenha = "Senha12";
		cadastrarUsuario(email, senha);
		SolicitarResetSenhaRequestDTO solicitarResetSenhaDto = SolicitarResetSenhaRequestDTO.builder()
			.email(email)
			.build();
		when(geradorCodigo.gerarCodigo()).thenReturn(codigoEsperado);
		var emailEnviado = usuarioService.solicitarResetSenha(solicitarResetSenhaDto);
		assertTrue(emailEnviado);
		AlterarSenhaRequestDTO alterarSenhaRequestDTO = AlterarSenhaRequestDTO.builder()
			.codigo(codigoEsperado)
			.novaSenha(novaSenha)
			.build();
		assertThrows(SenhaInvalidaException.class, () -> {
			usuarioService.alterarSenha(alterarSenhaRequestDTO);
		});
	}

	@Test
	@DisplayName("Deve lançar exceção ao tentar alterar senha usuário sem solicitação")
	void deveLancarExcecaoTentarAlterarSenhaUsuarioSemSolicitacao() {
		String codigoEsperado = UUID.randomUUID().toString();
		String email = "email@gmail.com";
		String senha = "Senha123";
		String novaSenha = "Senha12";
		cadastrarUsuario(email, senha);
		AlterarSenhaRequestDTO alterarSenhaRequestDTO = AlterarSenhaRequestDTO.builder()
			.codigo(codigoEsperado)
			.novaSenha(novaSenha)
			.build();
		assertThrows(SolicitacaoExpiradaExpcetion.class, () -> {
			usuarioService.alterarSenha(alterarSenhaRequestDTO);
		});
	}

	@Test
	@DisplayName("Deve cadastrar um usuário vinculado a um administrador")
	void deveCadastrarUsuarioVinculadoAdministrador() {
		var administrador = cadastrarUsuario("admin@gmail.com", "Senh1234!");
		var funcao = funcaoRepository.findAll();
		var email = UUID.randomUUID().toString().concat("@gmail.com");
		var senha = "Senha123!";
		CadastrarUsuarioRequestDTO cadastrarUsuarioRequestDTO = CadastrarUsuarioRequestDTO.builder()
			.nome("Usuario teste administrador")
			.email(email)
			.senha(senha)
			.funcaoId(funcao.getFirst().getId())
			.administradorVinculado(administrador.getId())
			.build();
		var usuario = usuarioService.cadastrarUsuario(cadastrarUsuarioRequestDTO);
		assertNotNull(usuario);
		assertTrue(BCrypt.checkpw("Senha123!", usuario.getSenha().getSenha()));
		var usuarioSalvo = usuarioRepository.findById(usuario.getId())
			.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
		assertNotNull(usuario.getAdministradorVinculado());
		assertEquals(administrador.getId(), usuarioSalvo.getAdministradorVinculado().getId());
		var usuarioFuncao = usuarioFuncaoRepository.findByUsuarioId(usuario.getId());
		assertEquals(funcao.getFirst().getId(), usuarioFuncao.getFirst().getFuncao().getId());
	}

	@Test
	@DisplayName("Deve ser possível redefinir a senha de um usuário vinculado com sucesso")
	void deveSerPossivelRedefinirSenhaUsuarioVinculadoComSucesso() {
		String codigoEsperado = UUID.randomUUID().toString();
		String email = "email@gmail.com";
		String emailUsuario = "email1@gmail.com";
		String senha = "Senha123";
		String novaSenha = "Senha123456";
		var administrador = cadastrarUsuario(email, senha);
		cadastrarUsuarioVinculado(emailUsuario, senha, administrador.getId());
		SolicitarResetSenhaRequestDTO solicitarResetSenhaDto = SolicitarResetSenhaRequestDTO.builder()
			.email(emailUsuario)
			.build();
		when(geradorCodigo.gerarCodigo()).thenReturn(codigoEsperado);
		var emailEnviado = usuarioService.solicitarResetSenha(solicitarResetSenhaDto);
		assertTrue(emailEnviado);
		AlterarSenhaRequestDTO alterarSenhaRequestDTO = AlterarSenhaRequestDTO.builder()
			.codigo(codigoEsperado)
			.novaSenha(novaSenha)
			.build();
		usuarioService.alterarSenha(alterarSenhaRequestDTO);
		var usuarioAlterado = usuarioRepository.findByEmail(new Email(emailUsuario))
			.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
		assertTrue(BCrypt.checkpw(novaSenha, usuarioAlterado.getSenha().getSenha()));
	}

	private Usuario cadastrarUsuario(String email, String senha) {
		var funcao = funcaoRepository.findAll();
		CadastrarUsuarioRequestDTO cadastrarUsuarioRequestDTO = CadastrarUsuarioRequestDTO.builder()
			.nome("Usuario teste")
			.email(email)
			.senha(senha)
			.funcaoId(funcao.getFirst().getId())
			.build();
		return usuarioService.cadastrarUsuario(cadastrarUsuarioRequestDTO);
	}

	private Usuario cadastrarUsuarioVinculado(String email, String senha, Long administradorId) {
		var funcao = funcaoRepository.findAll();
		CadastrarUsuarioRequestDTO cadastrarUsuarioRequestDTO = CadastrarUsuarioRequestDTO.builder()
			.nome("Usuario teste")
			.email(email)
			.senha(senha)
			.funcaoId(funcao.getFirst().getId())
			.administradorVinculado(administradorId)
			.build();
		return usuarioService.cadastrarUsuario(cadastrarUsuarioRequestDTO);
	}

}

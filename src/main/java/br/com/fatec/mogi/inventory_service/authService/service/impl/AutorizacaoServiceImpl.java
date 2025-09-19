package br.com.fatec.mogi.inventory_service.authService.service.impl;

import br.com.fatec.mogi.inventory_service.authService.domain.enums.TipoCache;
import br.com.fatec.mogi.inventory_service.authService.domain.exception.FuncionalidadeNaoMapeadaException;
import br.com.fatec.mogi.inventory_service.authService.domain.exception.UsuarioNaoAutenticadoException;
import br.com.fatec.mogi.inventory_service.authService.domain.exception.UsuarioNaoAutorizadoException;
import br.com.fatec.mogi.inventory_service.authService.domain.model.Usuario;
import br.com.fatec.mogi.inventory_service.authService.repository.FuncionalidadeRepository;
import br.com.fatec.mogi.inventory_service.authService.repository.UsuarioRepository;
import br.com.fatec.mogi.inventory_service.authService.service.AutorizacaoService;
import br.com.fatec.mogi.inventory_service.authService.service.RedisService;
import br.com.fatec.mogi.inventory_service.authService.web.dto.request.AutorizarUsuarioRequestDTO;
import br.com.fatec.mogi.inventory_service.common.filter.HttpAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AutorizacaoServiceImpl implements AutorizacaoService {

	private final FuncionalidadeRepository funcionalidadeRepository;

	private final UsuarioRepository usuarioRepository;

	private final RedisService redisService;

	private static final Logger LOG = Logger.getLogger(HttpAuthorizationFilter.class.getName());

	@Override
	public void autorizar(AutorizarUsuarioRequestDTO dto, String accessToken) {
		LOG.info("Verificando autorização para: " + dto);
		var funcionalidade = funcionalidadeRepository
			.findByEndpointAndHttpMethod(dto.getEndpoint(), dto.getHttpMethod())
			.orElseThrow(FuncionalidadeNaoMapeadaException::new);
		LOG.info("Buscando cache da sessão do usuário " + accessToken);
		var usuario = (Usuario) redisService.buscar(TipoCache.SESSAO_USUARIO, accessToken);
		if (usuario == null) {
			LOG.info("Usuário não encontrado!");
			throw new UsuarioNaoAutenticadoException();
		}
		if (!usuarioRepository.possuiFuncionalidade(usuario.getId(), funcionalidade.getFuncionalidade())) {
			LOG.info("Usuário não possui autorização");
			throw new UsuarioNaoAutorizadoException();
		}
		LOG.info("Usuário autorizado!");
	}

}

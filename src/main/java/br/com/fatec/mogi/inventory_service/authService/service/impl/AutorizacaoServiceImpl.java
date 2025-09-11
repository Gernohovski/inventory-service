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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutorizacaoServiceImpl implements AutorizacaoService {

	private final FuncionalidadeRepository funcionalidadeRepository;

	private final UsuarioRepository usuarioRepository;

	private final RedisService redisService;

	@Override
	public void autorizar(AutorizarUsuarioRequestDTO dto, String accessToken) {
		funcionalidadeRepository.findByFuncionalidade(dto.getFuncionalidade())
			.orElseThrow(FuncionalidadeNaoMapeadaException::new);
		var usuario = (Usuario) redisService.buscar(TipoCache.SESSAO_USUARIO, accessToken);
		if (usuario == null) {
			throw new UsuarioNaoAutenticadoException();
		}
		if (!usuarioRepository.possuiFuncionalidade(usuario.getId(), dto.getFuncionalidade())) {
			throw new UsuarioNaoAutorizadoException();
		}
	}

}

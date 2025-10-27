package br.com.fatec.mogi.inventory_service.common.web.context;

import br.com.fatec.mogi.inventory_service.authService.domain.model.Usuario;

public class RequestContext {

	private static final ThreadLocal<Usuario> usuarioThreadLocal = new ThreadLocal<>();

	private RequestContext() {
	}

	public static void setUsuario(Usuario usuario) {
		usuarioThreadLocal.set(usuario);
	}

	public static Usuario getUsuario() {
		return usuarioThreadLocal.get();
	}

	public static void clear() {
		usuarioThreadLocal.remove();
	}

}

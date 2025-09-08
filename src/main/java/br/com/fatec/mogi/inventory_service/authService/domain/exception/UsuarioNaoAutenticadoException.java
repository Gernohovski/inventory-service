package br.com.fatec.mogi.inventory_service.authService.domain.exception;

public class UsuarioNaoAutenticadoException extends RuntimeException {

	public UsuarioNaoAutenticadoException() {
		super("Usuário não autenticado.");
	}

}

package br.com.fatec.mogi.inventory_service.authService.domain.exception;

public class UsuarioNaoEncontradoException extends IllegalArgumentException {

	public UsuarioNaoEncontradoException(String message) {
		super(message);
	}

}

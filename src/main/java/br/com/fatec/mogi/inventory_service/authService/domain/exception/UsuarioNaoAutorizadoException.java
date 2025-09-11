package br.com.fatec.mogi.inventory_service.authService.domain.exception;

public class UsuarioNaoAutorizadoException extends RuntimeException {

	public UsuarioNaoAutorizadoException() {
		super("Usuário não possui autorização para realizar esta ação.");
	}

}

package br.com.fatec.mogi.inventory_service.authService.domain.exception;

public class LoginInvalidoException extends RuntimeException {

	public LoginInvalidoException() {
		super("E-mail ou senha inválidos.");
	}

}

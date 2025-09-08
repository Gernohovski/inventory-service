package br.com.fatec.mogi.inventory_service.authService.domain.exception;

public class UsuariosDivergentesException extends RuntimeException {

	public UsuariosDivergentesException() {
		super("O e-mail informado não coincide com a solicitação.");
	}

}

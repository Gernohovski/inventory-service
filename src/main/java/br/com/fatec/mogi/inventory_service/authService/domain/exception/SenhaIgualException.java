package br.com.fatec.mogi.inventory_service.authService.domain.exception;

public class SenhaIgualException extends IllegalArgumentException {

	public SenhaIgualException() {
		super("Você não pode usar a mesma senha.");
	}

}

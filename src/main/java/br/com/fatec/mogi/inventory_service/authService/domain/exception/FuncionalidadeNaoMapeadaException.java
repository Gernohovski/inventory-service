package br.com.fatec.mogi.inventory_service.authService.domain.exception;

public class FuncionalidadeNaoMapeadaException extends RuntimeException {

	public FuncionalidadeNaoMapeadaException() {
		super("Funcionalidade não mapeada.");
	}

}

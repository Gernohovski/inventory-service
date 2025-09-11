package br.com.fatec.mogi.inventory_service.coreService.domain.exception;

public class TipoEntradaNaoEncontradaException extends RuntimeException {

	public TipoEntradaNaoEncontradaException() {
		super("Tipo de entrada informado não encontrado.");
	}

}

package br.com.fatec.mogi.inventory_service.coreService.domain.exception;

public class StatusItemNaoEncontradoException extends RuntimeException {

	public StatusItemNaoEncontradoException() {
		super("Status informado não encontrado.");
	}

}

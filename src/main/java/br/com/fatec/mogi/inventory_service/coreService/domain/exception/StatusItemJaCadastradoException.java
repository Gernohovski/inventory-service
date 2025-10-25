package br.com.fatec.mogi.inventory_service.coreService.domain.exception;

public class StatusItemJaCadastradoException extends IllegalArgumentException {

	public StatusItemJaCadastradoException() {
		super("Status já cadastrado.");
	}

}

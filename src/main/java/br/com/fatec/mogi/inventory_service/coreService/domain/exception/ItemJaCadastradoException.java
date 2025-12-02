package br.com.fatec.mogi.inventory_service.coreService.domain.exception;

public class ItemJaCadastradoException extends RuntimeException {

	public ItemJaCadastradoException() {
		super("Já existe um código cadastrado para esse número de patrimônio.");
	}

}

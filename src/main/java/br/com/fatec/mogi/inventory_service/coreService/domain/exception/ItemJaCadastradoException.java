package br.com.fatec.mogi.inventory_service.coreService.domain.exception;

public class ItemJaCadastradoException extends RuntimeException {

	public ItemJaCadastradoException() {
		super("O item para esse codigo ja foi cadastrado.");
	}

}

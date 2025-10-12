package br.com.fatec.mogi.inventory_service.coreService.domain.exception;

public class NenhumItemEncontradoException extends RuntimeException {

	public NenhumItemEncontradoException() {
		super("Nenhum item foi encontrado");
	}

}

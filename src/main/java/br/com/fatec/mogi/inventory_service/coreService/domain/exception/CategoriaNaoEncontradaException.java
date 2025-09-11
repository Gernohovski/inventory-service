package br.com.fatec.mogi.inventory_service.coreService.domain.exception;

public class CategoriaNaoEncontradaException extends RuntimeException {

	public CategoriaNaoEncontradaException() {
		super("Categoria informada não encontrada.");
	}

}

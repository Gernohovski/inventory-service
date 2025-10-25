package br.com.fatec.mogi.inventory_service.coreService.domain.exception;

public class CategoriaNaoPodeSerExcluidaException extends RuntimeException {

	public CategoriaNaoPodeSerExcluidaException() {
		super("Categoria não pode ser excluída por estar vinculada a um item.");
	}

}

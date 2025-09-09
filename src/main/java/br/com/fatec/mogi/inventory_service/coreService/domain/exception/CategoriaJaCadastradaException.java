package br.com.fatec.mogi.inventory_service.coreService.domain.exception;

public class CategoriaJaCadastradaException extends IllegalArgumentException {

	public CategoriaJaCadastradaException(String nomeCategoria) {
		super("Categoria já cadastrada: " + nomeCategoria);
	}

}

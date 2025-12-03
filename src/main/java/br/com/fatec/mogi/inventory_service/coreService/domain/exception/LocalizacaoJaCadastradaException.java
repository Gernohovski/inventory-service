package br.com.fatec.mogi.inventory_service.coreService.domain.exception;

public class LocalizacaoJaCadastradaException extends RuntimeException {

	public LocalizacaoJaCadastradaException() {
		super("Localização já cadastrada.");
	}

}

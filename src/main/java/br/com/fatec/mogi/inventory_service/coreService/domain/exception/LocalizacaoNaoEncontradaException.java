package br.com.fatec.mogi.inventory_service.coreService.domain.exception;

public class LocalizacaoNaoEncontradaException extends RuntimeException {

	public LocalizacaoNaoEncontradaException() {
		super("Localização informada não encontrada.");
	}

}

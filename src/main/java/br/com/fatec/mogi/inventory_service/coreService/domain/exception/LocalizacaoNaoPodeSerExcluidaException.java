package br.com.fatec.mogi.inventory_service.coreService.domain.exception;

public class LocalizacaoNaoPodeSerExcluidaException extends RuntimeException {

	public LocalizacaoNaoPodeSerExcluidaException() {
		super("A localização não pode ser excluída pois está vinculada a um item.");
	}

}

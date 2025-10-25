package br.com.fatec.mogi.inventory_service.coreService.domain.exception;

public class StatusItemNaoPodeSerExcluidoException extends IllegalArgumentException {

	public StatusItemNaoPodeSerExcluidoException() {
		super("O status não pode ser excluído pois está vinculado a um item.");
	}

}

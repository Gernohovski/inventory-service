package br.com.fatec.mogi.inventory_service.auditService.domain.exception;

public class ItemNaoSendoAuditadoException extends IllegalArgumentException {

	public ItemNaoSendoAuditadoException() {
		super("O item não está sendo auditado.");
	}

}

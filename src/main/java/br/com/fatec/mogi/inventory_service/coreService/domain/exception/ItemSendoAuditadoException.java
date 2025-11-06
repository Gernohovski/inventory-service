package br.com.fatec.mogi.inventory_service.coreService.domain.exception;

public class ItemSendoAuditadoException extends IllegalArgumentException {

	public ItemSendoAuditadoException() {
		super("Não é possível excluir o item pois ele está sendo auditado no momento.");
	}

}

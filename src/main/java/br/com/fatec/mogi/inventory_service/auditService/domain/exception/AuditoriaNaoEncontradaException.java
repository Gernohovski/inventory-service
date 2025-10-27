package br.com.fatec.mogi.inventory_service.auditService.domain.exception;

public class AuditoriaNaoEncontradaException extends IllegalArgumentException {

	public AuditoriaNaoEncontradaException() {
		super("Auditoria não encontrada no histórico.");
	}

}

package br.com.fatec.mogi.inventory_service.auditService.domain.exception;

public class AuditoriaJaAcontecendoException extends IllegalArgumentException {

	public AuditoriaJaAcontecendoException() {
		super("Já há uma auditoria já acontecendo no momento.");
	}

}

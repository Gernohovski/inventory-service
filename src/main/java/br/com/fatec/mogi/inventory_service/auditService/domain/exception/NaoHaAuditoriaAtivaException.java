package br.com.fatec.mogi.inventory_service.auditService.domain.exception;

public class NaoHaAuditoriaAtivaException extends IllegalArgumentException {

	public NaoHaAuditoriaAtivaException() {
		super("Não há nenhuma auditoria ativa no momento.");
	}

}

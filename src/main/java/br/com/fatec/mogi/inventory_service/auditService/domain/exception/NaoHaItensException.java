package br.com.fatec.mogi.inventory_service.auditService.domain.exception;

public class NaoHaItensException extends IllegalArgumentException {

	public NaoHaItensException() {
		super("Não há itens a serem auditados.");
	}

}

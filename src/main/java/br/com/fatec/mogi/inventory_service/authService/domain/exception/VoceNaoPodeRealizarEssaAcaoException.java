package br.com.fatec.mogi.inventory_service.authService.domain.exception;

public class VoceNaoPodeRealizarEssaAcaoException extends IllegalArgumentException {

	public VoceNaoPodeRealizarEssaAcaoException() {
		super("Você não pode realizar essa ação pois há uma auditoria ativa.");
	}

}

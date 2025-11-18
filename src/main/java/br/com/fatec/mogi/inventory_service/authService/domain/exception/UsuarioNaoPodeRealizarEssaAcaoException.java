package br.com.fatec.mogi.inventory_service.authService.domain.exception;

public class UsuarioNaoPodeRealizarEssaAcaoException extends IllegalArgumentException {

	public UsuarioNaoPodeRealizarEssaAcaoException() {
		super("Você não pode realizar essa ação pois não tem permissão de auditoria.");
	}

}

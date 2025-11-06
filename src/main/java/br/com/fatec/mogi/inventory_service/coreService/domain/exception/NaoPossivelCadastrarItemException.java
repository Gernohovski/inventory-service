package br.com.fatec.mogi.inventory_service.coreService.domain.exception;

public class NaoPossivelCadastrarItemException extends IllegalArgumentException {

	public NaoPossivelCadastrarItemException() {
		super("Não é possível cadastrar itens no momento. Há uma auditoria ativa.");
	}

}

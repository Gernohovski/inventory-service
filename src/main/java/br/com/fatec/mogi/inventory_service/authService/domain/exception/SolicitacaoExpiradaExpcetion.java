package br.com.fatec.mogi.inventory_service.authService.domain.exception;

public class SolicitacaoExpiradaExpcetion extends RuntimeException {

	public SolicitacaoExpiradaExpcetion() {
		super("A solicitação de redefinição de senha expirou! Solicite novamente.");
	}

}

package br.com.fatec.mogi.inventory_service.coreService.domain.exception;

public class ArquivoNaoSuportadoException extends IllegalArgumentException {

	public ArquivoNaoSuportadoException() {
		super("Apenas arquivos CSV são permitidos");
	}

}

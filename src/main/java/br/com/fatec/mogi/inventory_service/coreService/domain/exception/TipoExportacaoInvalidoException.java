package br.com.fatec.mogi.inventory_service.coreService.domain.exception;

public class TipoExportacaoInvalidoException extends RuntimeException {

	public TipoExportacaoInvalidoException() {
		super("Tipo de arquivo de exportação inválido");
	}

}

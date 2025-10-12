package br.com.fatec.mogi.inventory_service.coreService.domain.exception;

public class ErroExportarItensException extends RuntimeException {

	public ErroExportarItensException() {
		super("Ocorreu um erro ao exportar itens.");
	}

}

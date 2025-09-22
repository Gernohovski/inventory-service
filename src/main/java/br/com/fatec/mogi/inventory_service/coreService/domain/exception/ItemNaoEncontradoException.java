package br.com.fatec.mogi.inventory_service.coreService.domain.exception;

public class ItemNaoEncontradoException extends RuntimeException {

    public ItemNaoEncontradoException() {
        super("Item não encontrado.");
    }

}

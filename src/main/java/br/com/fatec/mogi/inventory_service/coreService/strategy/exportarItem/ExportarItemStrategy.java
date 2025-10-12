package br.com.fatec.mogi.inventory_service.coreService.strategy.exportarItem;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ExportarItemStrategy {

	ResponseEntity<byte[]> exportar(List<Long> itensId);

}

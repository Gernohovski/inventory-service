package br.com.fatec.mogi.inventory_service.auditService.service;

import br.com.fatec.mogi.inventory_service.auditService.domain.model.Auditoria;
import br.com.fatec.mogi.inventory_service.auditService.domain.model.AuditoriaHistorico;

public interface AuditoriaHistoricoService {

	String gerarCodigoAuditoria();

	AuditoriaHistorico arquivarAuditoria(Auditoria auditoria);

}

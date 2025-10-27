package br.com.fatec.mogi.inventory_service.auditService.service;

import br.com.fatec.mogi.inventory_service.auditService.web.dto.request.AuditarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.auditService.web.dto.request.ConsultarHistoricoAuditoriaRequestDTO;
import br.com.fatec.mogi.inventory_service.auditService.web.dto.response.AuditoriaAtivaResponseDTO;
import br.com.fatec.mogi.inventory_service.auditService.web.dto.response.AuditoriaHistoricoDetalhadaResponseDTO;
import br.com.fatec.mogi.inventory_service.auditService.web.dto.response.AuditoriaHistoricoResponseDTO;
import br.com.fatec.mogi.inventory_service.common.web.response.CustomPageResponseDTO;
import org.springframework.data.domain.Pageable;

public interface AuditoriaService {

	void iniciarAuditoria();

	void auditarItem(AuditarItemRequestDTO dto);

	AuditoriaAtivaResponseDTO auditoriaAtiva();

	void encerrarAuditoria();

	CustomPageResponseDTO<AuditoriaHistoricoResponseDTO> consultarHistorico(ConsultarHistoricoAuditoriaRequestDTO dto,
			Pageable pageable);

	AuditoriaHistoricoDetalhadaResponseDTO buscarHistoricoPorCodigo(String codigoAuditoria);

}

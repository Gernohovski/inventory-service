package br.com.fatec.mogi.inventory_service.auditService.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditoriaAtivaResponseDTO {

	private String codigoAuditoria;

	private BigDecimal porcentagem;

	private Long quantidadeAuditados;

	private Long quantidadeNaoAuditados;

}

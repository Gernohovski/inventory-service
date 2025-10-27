package br.com.fatec.mogi.inventory_service.auditService.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditoriaHistoricoResponseDTO {

	private Long id;

	private String codigoAuditoria;

	private LocalDateTime dataInicio;

	private LocalDateTime dataFim;

	private String usuarioResponsavelNome;

	private Long totalItens;

	private Long itensAuditados;

	private Long itensNaoAuditados;

	private BigDecimal porcentagemConclusao;

}

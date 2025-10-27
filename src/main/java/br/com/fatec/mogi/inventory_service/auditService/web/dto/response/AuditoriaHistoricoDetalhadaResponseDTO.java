package br.com.fatec.mogi.inventory_service.auditService.web.dto.response;

import br.com.fatec.mogi.inventory_service.auditService.domain.model.ItemAuditado;
import br.com.fatec.mogi.inventory_service.auditService.domain.model.ItemAuditadoHistorico;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditoriaHistoricoDetalhadaResponseDTO {

	private Long id;

	private String codigoAuditoria;

	private LocalDateTime dataInicio;

	private LocalDateTime dataFim;

	private String usuarioResponsavelNome;

	private Long totalItens;

	private Long itensAuditados;

	private Long itensNaoAuditados;

	private BigDecimal porcentagemConclusao;

	private List<ItemAuditadoHistorico> itens;

}

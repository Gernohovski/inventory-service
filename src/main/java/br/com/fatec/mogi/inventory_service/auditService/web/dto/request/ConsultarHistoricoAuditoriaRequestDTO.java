package br.com.fatec.mogi.inventory_service.auditService.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultarHistoricoAuditoriaRequestDTO {

	private LocalDateTime dataInicio;

	private LocalDateTime dataFim;

}

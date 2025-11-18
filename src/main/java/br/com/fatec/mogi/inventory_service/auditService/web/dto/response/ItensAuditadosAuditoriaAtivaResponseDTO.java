package br.com.fatec.mogi.inventory_service.auditService.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItensAuditadosAuditoriaAtivaResponseDTO {

	private String codigoAuditoria;

	private List<ItemAuditadoResponseDTO> itensLocalizados;

	private List<ItemAuditadoResponseDTO> itensNaoLocalizados;

}

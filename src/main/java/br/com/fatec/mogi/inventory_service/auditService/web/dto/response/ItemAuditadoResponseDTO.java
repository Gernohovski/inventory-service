package br.com.fatec.mogi.inventory_service.auditService.web.dto.response;

import br.com.fatec.mogi.inventory_service.coreService.web.response.ItemResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemAuditadoResponseDTO {

	private ItemResponseDTO item;

	private Boolean conformidade;

	private Boolean localizado;

	private String observacao;

}

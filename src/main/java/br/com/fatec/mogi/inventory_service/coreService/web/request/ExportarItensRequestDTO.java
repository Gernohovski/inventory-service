package br.com.fatec.mogi.inventory_service.coreService.web.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportarItensRequestDTO {

	private List<Long> itensId;

}

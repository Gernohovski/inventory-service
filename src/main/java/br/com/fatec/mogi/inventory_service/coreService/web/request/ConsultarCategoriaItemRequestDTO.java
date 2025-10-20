package br.com.fatec.mogi.inventory_service.coreService.web.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultarCategoriaItemRequestDTO {

	private String termoPesquisa;

}

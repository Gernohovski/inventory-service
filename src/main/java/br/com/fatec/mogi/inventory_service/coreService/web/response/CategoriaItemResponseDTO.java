package br.com.fatec.mogi.inventory_service.coreService.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaItemResponseDTO {

	private Long id;

	private String nome;

}

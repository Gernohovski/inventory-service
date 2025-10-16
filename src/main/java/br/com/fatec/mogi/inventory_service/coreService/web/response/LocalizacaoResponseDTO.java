package br.com.fatec.mogi.inventory_service.coreService.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalizacaoResponseDTO {

	private Long id;

	private String nomeSala;

	private String andar;

	private Long quantidadeItens;

}

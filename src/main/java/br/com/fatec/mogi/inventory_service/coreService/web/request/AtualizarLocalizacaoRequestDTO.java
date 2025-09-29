package br.com.fatec.mogi.inventory_service.coreService.web.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarLocalizacaoRequestDTO {

	private String andar;

	private String nomeSala;

}

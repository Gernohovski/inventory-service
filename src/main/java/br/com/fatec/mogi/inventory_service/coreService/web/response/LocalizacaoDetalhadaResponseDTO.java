package br.com.fatec.mogi.inventory_service.coreService.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalizacaoDetalhadaResponseDTO {

	private String nome;

	private String andar;

	private List<ItemPorCategoriaResponseDTO> itensPorCategoria;

}

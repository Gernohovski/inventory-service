package br.com.fatec.mogi.inventory_service.coreService.web.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarItemRequestDTO {

	private String nomeItem;

	private String descricaoCurta;

	private String descricaoDetalhada;

	private String numeroSerie;

	private String codigoItem;

	private String notaFiscal;

	private Long categoriaItemId;

	private Long localizacaoId;

	private Long statusItemId;

	private Long tipoEntradaId;

}

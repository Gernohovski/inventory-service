package br.com.fatec.mogi.inventory_service.coreService.web.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultarItemRequestDTO {

	private LocalDateTime dataCadastroInicio;

	private LocalDateTime dataCadastroFim;

	private Long categoriaItemId;

	private Long localizacaoId;

	private Long statusItemId;

	private Long tipoEntradaId;

	private String nomeItem;

	private String codigoItem;

	private String numeroSerie;

	private String notaFiscal;

	private String termoPesquisa;

}

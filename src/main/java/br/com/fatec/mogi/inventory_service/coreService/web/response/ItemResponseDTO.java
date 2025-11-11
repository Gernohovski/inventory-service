package br.com.fatec.mogi.inventory_service.coreService.web.response;

import br.com.fatec.mogi.inventory_service.coreService.domain.model.CategoriaItem;
import br.com.fatec.mogi.inventory_service.coreService.domain.model.Localizacao;
import br.com.fatec.mogi.inventory_service.coreService.domain.model.StatusItem;
import br.com.fatec.mogi.inventory_service.coreService.domain.model.TipoEntrada;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponseDTO {

	private Long id;

	private String nomeItem;

	private String numeroSerie;

	private StatusItem statusItem;

	private CategoriaItem categoriaItem;

	private TipoEntrada tipoEntrada;

	private String codigoItem;

	private LocalDateTime dataCadastro;

	private Localizacao localizacao;

	private String notaFiscal;

	private LocalDateTime ultimaVezAuditado;

}

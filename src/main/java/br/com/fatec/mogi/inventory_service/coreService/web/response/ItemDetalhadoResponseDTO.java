package br.com.fatec.mogi.inventory_service.coreService.web.response;

import br.com.fatec.mogi.inventory_service.auditService.domain.model.ItemAuditadoHistorico;
import br.com.fatec.mogi.inventory_service.coreService.domain.model.CategoriaItem;
import br.com.fatec.mogi.inventory_service.coreService.domain.model.Localizacao;
import br.com.fatec.mogi.inventory_service.coreService.domain.model.StatusItem;
import br.com.fatec.mogi.inventory_service.coreService.domain.model.TipoEntrada;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDetalhadoResponseDTO {

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

    private List<ItemAuditadoHistorico> itemAuditadoHistorico;

}

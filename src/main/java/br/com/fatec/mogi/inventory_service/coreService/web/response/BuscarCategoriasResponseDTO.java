package br.com.fatec.mogi.inventory_service.coreService.web.response;

import br.com.fatec.mogi.inventory_service.coreService.domain.model.CategoriaItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarCategoriasResponseDTO {

	List<CategoriaItem> categorias;

}

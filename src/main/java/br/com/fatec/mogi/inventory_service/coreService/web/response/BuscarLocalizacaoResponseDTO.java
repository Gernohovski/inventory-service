package br.com.fatec.mogi.inventory_service.coreService.web.response;

import br.com.fatec.mogi.inventory_service.coreService.domain.model.Localizacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarLocalizacaoResponseDTO {

	List<Localizacao> localizacao;

}

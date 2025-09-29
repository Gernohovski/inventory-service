package br.com.fatec.mogi.inventory_service.coreService.domain.mapper;

import br.com.fatec.mogi.inventory_service.coreService.domain.model.Localizacao;
import br.com.fatec.mogi.inventory_service.coreService.web.request.AtualizarLocalizacaoRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
		nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LocalizacaoMapper {

	Localizacao update(AtualizarLocalizacaoRequestDTO dto, @MappingTarget Localizacao localizacao);

}

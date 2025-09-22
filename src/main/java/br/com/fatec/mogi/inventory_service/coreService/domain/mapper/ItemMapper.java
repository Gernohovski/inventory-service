package br.com.fatec.mogi.inventory_service.coreService.domain.mapper;

import br.com.fatec.mogi.inventory_service.coreService.domain.model.Item;
import br.com.fatec.mogi.inventory_service.coreService.web.request.AtualizarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.ItemResponseDTO;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ItemMapper {

    @Mapping(source = "categoriaItemId", target = "categoriaItem.id")
    @Mapping(source = "localizacaoId", target = "localizacao.id")
    @Mapping(source = "statusItemId", target = "statusItem.id")
    @Mapping(source = "tipoEntradaId", target = "tipoEntrada.id")
    Item from(CadastrarItemRequestDTO dto);

    @Mapping(source = "categoriaItemId", target = "categoriaItem.id")
    @Mapping(source = "localizacaoId", target = "localizacao.id")
    @Mapping(source = "statusItemId", target = "statusItem.id")
    @Mapping(source = "tipoEntradaId", target = "tipoEntrada.id")
    @Mapping(target = "categoriaItem", ignore = true)
    @Mapping(target = "tipoEntrada", ignore = true)
    @Mapping(target = "statusItem", ignore = true)
    @Mapping(target = "localizacao", ignore = true)
    Item update(AtualizarItemRequestDTO dto, @MappingTarget Item item);

    ItemResponseDTO from(Item item);


}

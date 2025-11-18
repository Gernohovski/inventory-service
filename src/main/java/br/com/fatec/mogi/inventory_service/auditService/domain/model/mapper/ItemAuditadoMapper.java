package br.com.fatec.mogi.inventory_service.auditService.domain.model.mapper;

import br.com.fatec.mogi.inventory_service.auditService.domain.model.ItemAuditado;
import br.com.fatec.mogi.inventory_service.auditService.web.dto.response.ItemAuditadoResponseDTO;
import br.com.fatec.mogi.inventory_service.coreService.domain.mapper.ItemMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = ItemMapper.class)
public interface ItemAuditadoMapper {

	ItemAuditadoResponseDTO from(ItemAuditado itemAuditado);

	List<ItemAuditadoResponseDTO> from(List<ItemAuditado> itensAuditados);

}

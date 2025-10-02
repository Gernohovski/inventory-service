package br.com.fatec.mogi.inventory_service.authService.web.dto.mapper;

import br.com.fatec.mogi.inventory_service.authService.domain.model.Usuario;
import br.com.fatec.mogi.inventory_service.authService.web.dto.response.UsuarioResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsuarioResponseDTOMapper {

	@Mapping(target = "email", source = "email.email")
	UsuarioResponseDTO from(Usuario usuario);

}

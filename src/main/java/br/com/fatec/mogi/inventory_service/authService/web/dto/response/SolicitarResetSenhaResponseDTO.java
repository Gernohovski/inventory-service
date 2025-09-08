package br.com.fatec.mogi.inventory_service.authService.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolicitarResetSenhaResponseDTO {

	private boolean emailEnviado;

}

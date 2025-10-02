package br.com.fatec.mogi.inventory_service.authService.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarUsuarioRequestDTO {

	private String nome;

	private String email;

	private boolean ativo;

}

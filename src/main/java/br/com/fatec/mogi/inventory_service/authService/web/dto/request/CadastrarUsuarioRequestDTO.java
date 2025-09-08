package br.com.fatec.mogi.inventory_service.authService.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CadastrarUsuarioRequestDTO {

	private Long administradorVinculado;

	private String nome;

	private String senha;

	private String email;

	private Long funcaoId;

}
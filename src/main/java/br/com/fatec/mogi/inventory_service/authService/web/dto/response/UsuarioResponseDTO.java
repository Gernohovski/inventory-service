package br.com.fatec.mogi.inventory_service.authService.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {

	private Long id;

	private String nome;

	private String email;

	private boolean ativo;

	private boolean canAudit;

    private String administradorVinculadoNome;

}

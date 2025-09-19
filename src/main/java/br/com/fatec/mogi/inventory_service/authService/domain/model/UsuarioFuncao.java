package br.com.fatec.mogi.inventory_service.authService.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(UsuarioFuncaoId.class)
public class UsuarioFuncao {

	@Id
	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	@Id
	@ManyToOne
	@JoinColumn(name = "funcao_id")
	private Funcao funcao;

}

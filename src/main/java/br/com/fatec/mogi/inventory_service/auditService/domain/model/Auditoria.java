package br.com.fatec.mogi.inventory_service.auditService.domain.model;

import br.com.fatec.mogi.inventory_service.authService.domain.model.Usuario;
import br.com.fatec.mogi.inventory_service.coreService.domain.model.EntidadeDominio;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Auditoria extends EntidadeDominio {

	@Column(unique = true, length = 20)
	private String codigoAuditoria;

	private LocalDateTime dataInicio;

	private LocalDateTime dataFim;

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuarioResponsavel;

	@OneToMany(mappedBy = "auditoria", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ItemAuditado> itensAuditados;

}

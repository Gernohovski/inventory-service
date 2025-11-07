package br.com.fatec.mogi.inventory_service.auditService.domain.model;

import br.com.fatec.mogi.inventory_service.authService.domain.model.Usuario;
import br.com.fatec.mogi.inventory_service.coreService.domain.model.EntidadeDominio;
import br.com.fatec.mogi.inventory_service.coreService.domain.model.Item;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ItemAuditado extends EntidadeDominio {

	@ManyToOne
	@JoinColumn(name = "auditoria_id", nullable = false)
	private Auditoria auditoria;

	@ManyToOne
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuarioResponsavel;

	private String observacao;

	private LocalDateTime dataVerificacao;

	private Boolean conformidade;

	private Boolean localizado;

}

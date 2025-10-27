package br.com.fatec.mogi.inventory_service.auditService.domain.model;

import br.com.fatec.mogi.inventory_service.coreService.domain.model.EntidadeDominio;
import jakarta.persistence.*;
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
@Table(name = "item_auditado_historico")
public class ItemAuditadoHistorico extends EntidadeDominio {

	@ManyToOne
	@JoinColumn(name = "auditoria_historico_id", nullable = false)
	private AuditoriaHistorico auditoriaHistorico;

	@Column(nullable = false)
	private Long itemId;

	@Column(nullable = false, length = 100)
	private String itemCodigo;

	@Column(nullable = false, length = 255)
	private String itemNome;

	@Column(length = 255)
	private String itemCategoria;

	@Column(length = 255)
	private String itemLocalizacao;

	@Column(length = 100)
	private String itemStatus;

	@Column(length = 100)
	private String itemNumeroSerie;

	@Column(columnDefinition = "TEXT")
	private String observacao;

	private LocalDateTime dataVerificacao;

	@Column(nullable = false)
	private Boolean conformidade;

}

package br.com.fatec.mogi.inventory_service.auditService.domain.model;

import br.com.fatec.mogi.inventory_service.coreService.domain.model.EntidadeDominio;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "auditoria_historico_id", nullable = false)
	@JsonIgnore
	@ToString.Exclude
	private AuditoriaHistorico auditoriaHistorico;

	@Column(nullable = false)
	private Long itemId;

	@Column(nullable = false, length = 100)
	private String itemCodigo;

	@Column(nullable = false)
	private String itemNome;

	@Column(length = 100)
	private String usuarioNome;

	private String itemCategoria;

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

	@Column(nullable = false)
	private Boolean localizado;

}

package br.com.fatec.mogi.inventory_service.auditService.domain.model;

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
@Table(name = "auditoria_historico")
public class AuditoriaHistorico extends EntidadeDominio {

	@Column(unique = true, nullable = false, length = 20)
	private String codigoAuditoria;

	@Column(nullable = false)
	private LocalDateTime dataInicio;

	@Column(nullable = false)
	private LocalDateTime dataFim;

	@Column(nullable = false)
	private Long usuarioResponsavelId;

	@Column(nullable = false, length = 255)
	private String usuarioResponsavelNome;

	@Column(nullable = false)
	private Long totalItens;

	@OneToMany(mappedBy = "auditoriaHistorico", cascade = CascadeType.ALL, orphanRemoval = true,
			fetch = FetchType.EAGER)
	private List<ItemAuditadoHistorico> itensAuditadosHistorico;

}

package br.com.fatec.mogi.inventory_service.coreService.domain.model;

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
public class Item extends EntidadeDominio {

	private String nomeItem;

	private String descricaoCurta;

	private String descricaoDetalhada;

	private String numeroSerie;

	@ManyToOne
	@JoinColumn(name = "status_item_id")
	private StatusItem statusItem;

	@ManyToOne
	@JoinColumn(name = "categoria_item_id")
	private CategoriaItem categoriaItem;

	@ManyToOne
	@JoinColumn(name = "tipo_entrada_id")
	private TipoEntrada tipoEntrada;

	private String codigoItem;

	private LocalDateTime dataCadastro;

	private LocalDateTime dataAlteracao;

	@ManyToOne
	@JoinColumn(name = "localizacao_id")
	private Localizacao localizacao;

	private String notaFiscal;

}

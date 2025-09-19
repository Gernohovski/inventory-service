package br.com.fatec.mogi.inventory_service.coreService.domain.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class Localizacao extends EntidadeDominio {

	private String andar;

	private String nomeSala;

}

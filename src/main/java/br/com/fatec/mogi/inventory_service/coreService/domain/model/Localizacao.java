package br.com.fatec.mogi.inventory_service.coreService.domain.model;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class Localizacao extends EntidadeDominio {

	private String andar;

	private String nomeSala;

}

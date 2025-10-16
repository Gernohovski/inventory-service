package br.com.fatec.mogi.inventory_service.coreService.domain.model;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Formula;

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

	@Formula("(SELECT COUNT(*) FROM item i WHERE i.localizacao_id = id)")
	private Long quantidadeItens;

}

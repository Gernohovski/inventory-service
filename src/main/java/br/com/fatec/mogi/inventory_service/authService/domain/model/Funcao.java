package br.com.fatec.mogi.inventory_service.authService.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Funcao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "funcao_funcionalidade", joinColumns = @JoinColumn(name = "funcao_id"),
			inverseJoinColumns = @JoinColumn(name = "funcionalidade_id"))
	@Builder.Default
	private Set<Funcionalidade> funcionalidades = new HashSet<>();

}

package br.com.fatec.mogi.inventory_service.authService.domain.model;

import br.com.fatec.mogi.inventory_service.authService.domain.model.valueObjects.Email;
import br.com.fatec.mogi.inventory_service.authService.domain.model.valueObjects.Senha;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;

	@Embedded
	private Senha senha;

	@Embedded
	private Email email;

	private boolean ativo;

	private LocalDateTime dataCriacao;

	private LocalDateTime dataAlteracao;

	@ManyToOne
	@JoinColumn(name = "administrador_id")
	private Usuario administradorVinculado;

	public Usuario(String nome, String senha, String email) {
		this.nome = nome;
		this.senha = new Senha(senha);
		this.email = new Email(email);
		this.ativo = true;
	}

	public Usuario(String nome, String senha, String email, Usuario administradorVinculado) {
		this.nome = nome;
		this.senha = new Senha(senha);
		this.email = new Email(email);
		this.administradorVinculado = administradorVinculado;
		this.ativo = true;
	}

}
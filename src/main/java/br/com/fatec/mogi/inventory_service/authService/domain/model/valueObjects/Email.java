package br.com.fatec.mogi.inventory_service.authService.domain.model.valueObjects;

import br.com.fatec.mogi.inventory_service.authService.domain.exception.EmailInvalidoException;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Embeddable
@ToString
public class Email {

	private String email;

	public Email(String email) {
		setEmail(email);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if (email == null || email.trim().isEmpty()) {
			throw new EmailInvalidoException("O e-mail não pode ser nulo ou vazio.");
		}
		String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
		if (!email.matches(emailRegex)) {
			throw new EmailInvalidoException("Formato de e-mail inválido.");
		}
		this.email = email;
	}

}

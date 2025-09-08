package br.com.fatec.mogi.inventory_service.authService.service;

import br.com.fatec.mogi.inventory_service.authService.domain.model.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@ActiveProfiles("integration")
public class EmailServiceTest {

	@Autowired
	private EmailService emailService;

	@Test
	@DisplayName("Deve retornar falso ao realizar envio de redefinição de senha para endereço inválido")
	void deveRetornarFalseRealizarEnvioRedefinicaoSenhaEnderecoInvalido() {
		Usuario usuario = new Usuario("Rodrigo", "Senha123", "usuariotodotorto@gmail.com");
		var enviado = emailService.enviarEmailResetSenha(usuario, usuario);
		assertFalse(enviado);
	}

}

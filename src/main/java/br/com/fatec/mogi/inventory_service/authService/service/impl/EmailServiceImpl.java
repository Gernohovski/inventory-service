package br.com.fatec.mogi.inventory_service.authService.service.impl;

import br.com.fatec.mogi.inventory_service.authService.domain.enums.TipoCache;
import br.com.fatec.mogi.inventory_service.authService.domain.model.Usuario;
import br.com.fatec.mogi.inventory_service.authService.service.EmailService;
import br.com.fatec.mogi.inventory_service.authService.service.RedisService;
import br.com.fatec.mogi.inventory_service.authService.utils.GeradorCodigo;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Profile("!test")
public class EmailServiceImpl implements EmailService {

	@Value("${spring.mail.username}")
	private String emailSender;

	@Value("${confirm.email.code.url}")
	private String confirmUrl;

	private final JavaMailSender mailSender;

	private final RedisService redisService;

	private final GeradorCodigo geradorCodigo;

	@Override
	public boolean enviarEmailResetSenha(Usuario usuario, Usuario administradorVinculado) {
		try {
			var codigo = geradorCodigo.gerarCodigo();
			
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			
			helper.setFrom(emailSender);
			helper.setTo(administradorVinculado.getEmail().getEmail());
			helper.setSubject("Redefinição de senha - Sistema de Inventário");
			
			String htmlContent = buildResetPasswordEmailHtml(usuario.getNome(), codigo);
			helper.setText(htmlContent, true);
			
			mailSender.send(message);
			redisService.salvar(TipoCache.CODIGO_RESET_SENHA, codigo, usuario, 36000L);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	private String buildResetPasswordEmailHtml(String userName, String code) {
		return """
			<!DOCTYPE html>
			<html>
			<head>
				<meta charset="UTF-8">
				<style>
					body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
					.container { max-width: 600px; margin: 0 auto; padding: 20px; }
					.header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; }
					.content { background-color: #f9f9f9; padding: 30px; border-radius: 5px; margin-top: 20px; }
					.code { background-color: #fff; border: 2px solid #4CAF50; padding: 15px; font-size: 24px; 
							font-weight: bold; text-align: center; letter-spacing: 5px; margin: 20px 0; }
					.footer { text-align: center; margin-top: 20px; color: #777; font-size: 12px; }
				</style>
			</head>
			<body>
				<div class="container">
					<div class="header">
						<h1>Redefinição de Senha</h1>
					</div>
					<div class="content">
						<p>Olá <strong>%s</strong>,</p>
						<p>Recebemos uma solicitação para redefinir sua senha no Sistema de Inventário.</p>
						<p>Use o código abaixo para redefinir sua senha:</p>
						<div class="code">%s</div>
						<p>Este código é válido por 10 horas.</p>
						<p>Se você não solicitou esta redefinição, por favor ignore este e-mail.</p>
					</div>
					<div class="footer">
						<p>Sistema de Inventário - FATEC Mogi das Cruzes</p>
					</div>
				</div>
			</body>
			</html>
			""".formatted(userName, code);
	}

}
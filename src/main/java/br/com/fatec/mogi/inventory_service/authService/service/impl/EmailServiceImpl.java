package br.com.fatec.mogi.inventory_service.authService.service.impl;

import br.com.fatec.mogi.inventory_service.authService.domain.enums.TipoCache;
import br.com.fatec.mogi.inventory_service.authService.domain.model.Usuario;
import br.com.fatec.mogi.inventory_service.authService.service.EmailService;
import br.com.fatec.mogi.inventory_service.authService.service.RedisService;
import br.com.fatec.mogi.inventory_service.authService.utils.GeradorCodigo;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
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
		log.info("Iniciando envio de e-mail de redefinição de senha para usuário: {} (ID: {})", 
				usuario.getNome(), usuario.getId());
		log.info("E-mail será enviado para: {}", administradorVinculado.getEmail().getEmail());
		
		try {
			var codigo = geradorCodigo.gerarCodigo();
			log.info("Código de redefinição gerado com sucesso");
			
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			
			helper.setFrom(emailSender);
			helper.setTo(administradorVinculado.getEmail().getEmail());
			helper.setSubject("Redefinição de senha - Sistema de Inventário");
			log.info("Remetente configurado: {}", emailSender);
			
			String htmlContent = buildResetPasswordEmailHtml(usuario.getNome(), codigo);
			helper.setText(htmlContent, true);
			log.info("Conteúdo HTML do e-mail preparado");
			
			mailSender.send(message);
			log.info("E-mail de redefinição de senha enviado com sucesso para: {}", 
					administradorVinculado.getEmail().getEmail());
			
			redisService.salvar(TipoCache.CODIGO_RESET_SENHA, codigo, usuario, 36000L);
			log.info("Código de redefinição salvo no Redis com TTL de 10 horas");
			
			return true;
		}
		catch (Exception e) {
			log.error("Erro ao enviar e-mail de redefinição de senha para usuário: {} (ID: {}). Destinatário: {}", 
					usuario.getNome(), usuario.getId(), administradorVinculado.getEmail().getEmail(), e);
			return false;
		}
	}

	private String buildResetPasswordEmailHtml(String userName, String code) {
		String resetUrl = confirmUrl + "?code=" + code;
		return "<!DOCTYPE html>" +
			"<html>" +
			"<head>" +
			"<meta charset=\"UTF-8\">" +
			"<style>" +
			"body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
			".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
			".header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; }" +
			".content { background-color: #f9f9f9; padding: 30px; border-radius: 5px; margin-top: 20px; }" +
			".code { background-color: #fff; border: 2px solid #4CAF50; padding: 15px; font-size: 24px; font-weight: bold; text-align: center; letter-spacing: 5px; margin: 20px 0; }" +
			".button { display: inline-block; padding: 15px 30px; margin: 20px 0; background-color: #4CAF50; color: white !important; text-decoration: none; border-radius: 5px; font-weight: bold; font-size: 16px; text-align: center; }" +
			".button:hover { background-color: #45a049; }" +
			".button-container { text-align: center; margin: 30px 0; }" +
			".divider { margin: 30px 0; text-align: center; color: #999; position: relative; }" +
			".divider::before, .divider::after { content: ''; position: absolute; top: 50%; width: 40%; height: 1px; background-color: #ddd; }" +
			".divider::before { left: 0; }" +
			".divider::after { right: 0; }" +
			".footer { text-align: center; margin-top: 20px; color: #777; font-size: 12px; }" +
			"</style>" +
			"</head>" +
			"<body>" +
			"<div class=\"container\">" +
			"<div class=\"header\">" +
			"<h1>Redefinição de Senha</h1>" +
			"</div>" +
			"<div class=\"content\">" +
			"<p>Olá <strong>" + userName + "</strong>,</p>" +
			"<p>Recebemos uma solicitação para redefinir sua senha no Sistema de Inventário.</p>" +
			"<div class=\"button-container\">" +
			"<a href=\"" + resetUrl + "\" class=\"button\">Redefinir Senha</a>" +
			"</div>" +
			"<div class=\"divider\">OU</div>" +
			"<p>Use o código abaixo para redefinir sua senha manualmente:</p>" +
			"<div class=\"code\">" + code + "</div>" +
			"<p>Este código é válido por 10 horas.</p>" +
			"<p>Se você não solicitou esta redefinição, por favor ignore este e-mail.</p>" +
			"</div>" +
			"<div class=\"footer\">" +
			"<p>Sistema de Inventário - FATEC Mogi das Cruzes</p>" +
			"</div>" +
			"</div>" +
			"</body>" +
			"</html>";
	}

}
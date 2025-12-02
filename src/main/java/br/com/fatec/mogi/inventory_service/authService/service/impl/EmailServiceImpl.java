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

	@Value("${auth.email.sender}")
	private String emailSender;

	@Value("${confirm.email.code.url}")
	private String confirmUrl;

	private final JavaMailSender mailSender;

	private final RedisService redisService;

	private final GeradorCodigo geradorCodigo;

	@Override
	public boolean enviarEmailResetSenha(Usuario usuario, Usuario administradorVinculado) {
		log.info("Iniciando envio de e-mail de redefinição de senha para usuário: {} (ID: {})", usuario.getNome(),
				usuario.getId());
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
		return "<!DOCTYPE html>" + "<html>" + "<head>" + "<meta charset=\"UTF-8\">" + "<style>"
				+ "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; background-color: #f4f4f4; margin: 0; padding: 20px; }"
				+ ".container { max-width: 600px; margin: 0 auto; background-color: white; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }"
				+ ".header { background-color: #941711; color: white; padding: 30px 20px; text-align: center; }"
				+ ".header h1 { margin: 0; font-size: 28px; }"
				+ ".content { background-color: white; padding: 40px 30px; }"
				+ ".button { display: inline-block; padding: 15px 40px; margin: 20px 0; background-color: #941711; color: white !important; text-decoration: none; border-radius: 5px; font-weight: bold; font-size: 16px; text-align: center; }"
				+ ".button:hover { background-color: #7a120e; }"
				+ ".button-container { text-align: center; margin: 30px 0; }"
				+ ".footer { background-color: #f9f9f9; text-align: center; padding: 20px; color: #777; font-size: 12px; border-top: 1px solid #e0e0e0; }"
				+ ".footer p { margin: 0; }" + "</style>" + "</head>" + "<body>" + "<div class=\"container\">"
				+ "<div class=\"header\">" + "<h1>Redefinição de Senha</h1>" + "</div>" + "<div class=\"content\">"
				+ "<p>Olá <strong>" + userName + "</strong>,</p>"
				+ "<p>Recebemos uma solicitação para redefinir sua senha no Sistema de Inventário.</p>"
				+ "<div class=\"button-container\">" + "<a href=\"" + resetUrl
				+ "\" class=\"button\">Redefinir Senha</a>" + "</div>" + "<p>Este código é válido por 10 horas.</p>"
				+ "<p>Se você não solicitou esta redefinição, por favor ignore este e-mail.</p>" + "</div>"
				+ "<div class=\"footer\">" + "<p>Sistema de Inventário - FATEC Mogi das Cruzes</p>" + "</div>"
				+ "</div>" + "</body>" + "</html>";
	}

}
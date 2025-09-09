package br.com.fatec.mogi.inventory_service.authService.service.fake;

import br.com.fatec.mogi.inventory_service.authService.domain.enums.TipoCache;
import br.com.fatec.mogi.inventory_service.authService.domain.model.Usuario;
import br.com.fatec.mogi.inventory_service.authService.service.EmailService;
import br.com.fatec.mogi.inventory_service.authService.service.RedisService;
import br.com.fatec.mogi.inventory_service.authService.utils.GeradorCodigo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Profile("test")
public class EmailServiceFake implements EmailService {

	private final GeradorCodigo geradorCodigo;

	private final RedisService redisService;

	@Override
	public boolean enviarEmailResetSenha(Usuario usuario, Usuario administradorVinculado) {
		var codigo = geradorCodigo.gerarCodigo();
		if (administradorVinculado.getEmail().getEmail().equals("emailinvalido@gmail.com")) {
			return false;
		}
		String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
		var valido = usuario.getEmail().getEmail().matches(emailRegex);
		if (valido) {
			redisService.salvar(TipoCache.CODIGO_RESET_SENHA, codigo, usuario, 36000L);
		}
		return valido;
	}

}

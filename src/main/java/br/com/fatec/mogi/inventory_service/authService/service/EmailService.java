package br.com.fatec.mogi.inventory_service.authService.service;

import br.com.fatec.mogi.inventory_service.authService.domain.model.Usuario;

public interface EmailService {

	boolean enviarEmailResetSenha(Usuario usuario, Usuario administradorVinculado);

}
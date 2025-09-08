package br.com.fatec.mogi.inventory_service.authService.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TipoCache {

	REFRESH_TOKEN("cache-refresh-token"), SESSAO_USUARIO("cache-sessao-usuario"),
	CODIGO_RESET_SENHA("cache-codigo-reset-senha");

	private final String nome;

}

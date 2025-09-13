package br.com.fatec.mogi.inventory_service.common.domain.enums;

import lombok.Getter;

@Getter
public enum FuncionalidadesEnum {

	LOGIN("/auth-service/v1/autenticacao/login"), REFRESH_TOKEN("/auth-service/v1/autenticacao/refresh"),
	LOGOUT("/auth-service/v1/autenticacao/logout"), CADASTRO_USUARIO("/auth-service/v1/usuarios"),
	SOLICITAR_REDEFINICAO_SENHA("/auth-service/v1/usuarios/solicitar-redefinicao-senha"),
	ALTERAR_SENHA("/auth-service/v1/usuarios/alterar-senha"), LISTAR_CATEGORIA("/core-service/v1/categorias"),
	CADASTRAR_CATEGORIA("/core-service/v1/categorias"), CADASTRAR_ITEM("/core-service/v1/itens"),
	FILTRAR_ITENS("/core-service/v1/itens"), BUSCAR_LOCALIZACAO("/core-service/v1/localizacao"),
	CADASTRO_USUARIO_RELACIONADO("/api/usuarios/relacionados"),
	REDEFINIR_SENHA("/auth-service/v1/usuarios/alterar-senha"), DELETAR_CATEGORIA("/api/categorias/delete"),
	CADASTRO_LOCALIZACAO("/api/localizacoes/create"), DELETAR_LOCALIZACAO("/api/localizacoes/delete"),
	LISTAR_LOCALIZACAO("/api/localizacoes"), EDITAR_ITEM("/api/itens/edit"), ALTERAR_STATUS_ITEM("/api/itens/status"),
	DELETAR_ITEM("/api/itens/delete"), LISTAR_ITEM("/api/itens/list"), GERAR_RELATORIO_ITEM("/api/relatorios/itens"),
	INICIAR_AUDITORIA("/api/auditorias/iniciar"), GERAR_RELATORIO_AUDITORIA("/api/relatorios/auditorias"),
	CONFIRMAR_LOCALIZACAO("/api/auditorias/confirmar-localizacao"), FINALIZAR_AUDITORIA("/api/auditorias/finalizar");

	private final String endpoint;

	FuncionalidadesEnum(String endpoint) {
		this.endpoint = endpoint;
	}

	public static FuncionalidadesEnum getByEndpoint(String endpoint) {
		for (FuncionalidadesEnum funcionalidade : values()) {
			if (funcionalidade.getEndpoint().equals(endpoint)) {
				return funcionalidade;
			}
		}
		return null;
	}

}

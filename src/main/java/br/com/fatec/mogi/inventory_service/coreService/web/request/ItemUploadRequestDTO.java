package br.com.fatec.mogi.inventory_service.coreService.web.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemUploadRequestDTO {

	private String codigo;

	private String localizacao;

	private String nome;

	private String categoria;

	private String condicao;

	private String tipoEntrada;

	private String dataCadastro;

	private Long numeroLinha;

}

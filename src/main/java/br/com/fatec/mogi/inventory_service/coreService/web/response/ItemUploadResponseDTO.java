package br.com.fatec.mogi.inventory_service.coreService.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemUploadResponseDTO {

	private List<UploadErrorResponseDTO> erros;

	private Long processadosComSucesso;

	private Long processadosComErro;

}

package br.com.fatec.mogi.inventory_service.coreService.service;

import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarLocalizacaoRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.BuscarLocalizacaoResponseDTO;

public interface LocalizacaoService {

	BuscarLocalizacaoResponseDTO buscar();

	void cadastrar(CadastrarLocalizacaoRequestDTO dto);

}

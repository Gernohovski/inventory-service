package br.com.fatec.mogi.inventory_service.coreService.service;

import br.com.fatec.mogi.inventory_service.common.web.response.CustomPageResponseDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.AtualizarLocalizacaoRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarLocalizacaoRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.BuscarLocalizacaoResponseDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.LocalizacaoResponseDTO;
import org.springframework.data.domain.Pageable;

public interface LocalizacaoService {

	BuscarLocalizacaoResponseDTO buscar();

	void cadastrar(CadastrarLocalizacaoRequestDTO dto);

	CustomPageResponseDTO<LocalizacaoResponseDTO> buscarPaginado(Pageable pageable);

	void atualizar(AtualizarLocalizacaoRequestDTO dto, Long id);

}

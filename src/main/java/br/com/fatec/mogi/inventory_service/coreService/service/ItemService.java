package br.com.fatec.mogi.inventory_service.coreService.service;

import br.com.fatec.mogi.inventory_service.common.web.response.CustomPageResponseDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.AtualizarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.ConsultarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.ExportarItensRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.ItemResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ItemService {

	ItemResponseDTO cadastrarItem(CadastrarItemRequestDTO dto);

	CustomPageResponseDTO<ItemResponseDTO> filtrarItems(ConsultarItemRequestDTO dto, Pageable pageable);

	ItemResponseDTO atualizar(AtualizarItemRequestDTO dto, Long id);

	void deletar(Long id);

	void upload(MultipartFile file) throws Exception;

	ResponseEntity<byte[]> exportar(ExportarItensRequestDTO dto, String tipo);

}

package br.com.fatec.mogi.inventory_service.coreService.web;

import br.com.fatec.mogi.inventory_service.common.web.response.CustomPageResponseDTO;
import br.com.fatec.mogi.inventory_service.coreService.service.ItemService;
import br.com.fatec.mogi.inventory_service.coreService.web.request.AtualizarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.ConsultarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.ItemResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/core-service/v1/itens")
public record ItemController(ItemService itemService) {

	@PostMapping
	public ResponseEntity<ItemResponseDTO> cadastrarItem(@RequestBody CadastrarItemRequestDTO dto,
			@RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		var item = itemService.cadastrarItem(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(item);
	}

	@GetMapping
	public ResponseEntity<CustomPageResponseDTO<ItemResponseDTO>> filtrarItems(
			@ModelAttribute ConsultarItemRequestDTO dto, @PageableDefault Pageable pageable,
			@RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		var items = itemService.filtrarItems(dto, pageable);
		return ResponseEntity.status(HttpStatus.OK).body(items);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ItemResponseDTO> atualizarItem(@PathVariable Long id, @RequestBody AtualizarItemRequestDTO dto, @RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		var item = itemService.atualizar(dto, id);
		return ResponseEntity.status(HttpStatus.OK).body(item);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletarItem(@PathVariable Long id, @RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		itemService.deletar(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}

package br.com.fatec.mogi.inventory_service.coreService.web;

import br.com.fatec.mogi.inventory_service.common.web.response.CustomPageResponseDTO;
import br.com.fatec.mogi.inventory_service.coreService.service.ItemService;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.ConsultarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.ItemResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/core-service/v1/itens")
public record ItemController(ItemService itemService) {

	@PostMapping
	public ResponseEntity<Void> cadastrarItem(@RequestBody CadastrarItemRequestDTO dto,
			@RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		itemService.cadastrarItem(dto, accessToken);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping
	public ResponseEntity<CustomPageResponseDTO<ItemResponseDTO>> filtrarItems(
			@ModelAttribute ConsultarItemRequestDTO dto, @PageableDefault Pageable pageable,
			@RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		var items = itemService.filtrarItems(dto, pageable, accessToken);
		return ResponseEntity.status(HttpStatus.OK).body(items);
	}
}

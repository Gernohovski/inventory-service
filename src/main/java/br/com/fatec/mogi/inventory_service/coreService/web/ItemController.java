package br.com.fatec.mogi.inventory_service.coreService.web;

import br.com.fatec.mogi.inventory_service.coreService.service.ItemService;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarItemRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}

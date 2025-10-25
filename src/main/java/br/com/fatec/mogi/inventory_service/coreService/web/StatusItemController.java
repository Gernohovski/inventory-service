package br.com.fatec.mogi.inventory_service.coreService.web;

import br.com.fatec.mogi.inventory_service.coreService.service.StatusItemService;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarStatusItemRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/core-service/v1/status")
public record StatusItemController(StatusItemService service) {

	@PostMapping
	public ResponseEntity<Void> cadastrar(@RequestHeader("X-ACCESS-TOKEN") String accessToken,
			@RequestBody CadastrarStatusItemRequestDTO dto) {
		service.cadastrar(dto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@RequestHeader("X-ACCESS-TOKEN") String accessToken,
			@PathVariable("id") Long id) {
		service.deletar(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}

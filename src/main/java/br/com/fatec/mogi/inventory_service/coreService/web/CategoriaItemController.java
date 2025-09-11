package br.com.fatec.mogi.inventory_service.coreService.web;

import br.com.fatec.mogi.inventory_service.coreService.service.CategoriaItemService;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarCategoriaItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.BuscarCategoriasResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/core-service/v1/categorias")
public record CategoriaItemController(CategoriaItemService categoriaItemService) {

	@GetMapping
	public ResponseEntity<BuscarCategoriasResponseDTO> buscar(@RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		var categorias = categoriaItemService.buscar(accessToken);
		return ResponseEntity.status(HttpStatus.OK).body(categorias);
	}

	@PostMapping
	public ResponseEntity<?> cadastrar(@RequestBody CadastrarCategoriaItemRequestDTO dto,
			@RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		categoriaItemService.cadastrar(dto, accessToken);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}

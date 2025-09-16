package br.com.fatec.mogi.inventory_service.coreService.web;

import br.com.fatec.mogi.inventory_service.coreService.service.CategoriaItemService;
import br.com.fatec.mogi.inventory_service.coreService.web.request.AtualizarCategoriaItemRequestDTO;
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
		var categorias = categoriaItemService.buscar();
		return ResponseEntity.status(HttpStatus.OK).body(categorias);
	}

	@PostMapping
	public ResponseEntity<?> cadastrar(@RequestBody CadastrarCategoriaItemRequestDTO dto,
			@RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		categoriaItemService.cadastrar(dto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletar(@PathVariable("id") Long id) {
		categoriaItemService.deletar(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> atualizar(@RequestBody AtualizarCategoriaItemRequestDTO dto, @PathVariable("id") Long id) {
		categoriaItemService.atualizar(dto, id);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

}

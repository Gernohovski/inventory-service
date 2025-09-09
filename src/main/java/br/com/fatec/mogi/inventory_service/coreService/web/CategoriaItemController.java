package br.com.fatec.mogi.inventory_service.coreService.web;

import br.com.fatec.mogi.inventory_service.coreService.service.CategoriaItemService;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarCategoriaItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.BuscarCategoriasResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/core-service/v1/categorias")
public record CategoriaItemController(CategoriaItemService categoriaItemService) {

	@GetMapping
	public ResponseEntity<BuscarCategoriasResponseDTO> buscar() {
		var categorias = categoriaItemService.buscar();
		return ResponseEntity.status(HttpStatus.OK).body(categorias);
	}

	@PostMapping
	public ResponseEntity<?> cadastrar(@RequestBody CadastrarCategoriaItemRequestDTO dto) {
		categoriaItemService.cadastrar(dto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}

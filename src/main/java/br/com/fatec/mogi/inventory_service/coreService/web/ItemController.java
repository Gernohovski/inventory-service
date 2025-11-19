package br.com.fatec.mogi.inventory_service.coreService.web;

import br.com.fatec.mogi.inventory_service.common.web.response.CustomPageResponseDTO;
import br.com.fatec.mogi.inventory_service.coreService.service.ItemService;
import br.com.fatec.mogi.inventory_service.coreService.web.request.AtualizarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.ConsultarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.ExportarItensRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.ItemDetalhadoResponseDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.ItemResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/buscar/{codigoItem}")
    public ResponseEntity<ItemResponseDTO> buscarItemPeloCodigo(@PathVariable("codigoItem") String codigoItem, @RequestHeader("X-ACCESS-TOKEN") String accessToken) {
        var item = itemService.buscarPeloCodigo(codigoItem);
        return ResponseEntity.status(HttpStatus.OK).body(item);
    }

	@GetMapping("/{id}")
	public ResponseEntity<ItemDetalhadoResponseDTO> buscarItem(@PathVariable("id") Long itemId, @RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		var item = itemService.buscar(itemId);
		return ResponseEntity.status(HttpStatus.OK).body(item);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ItemResponseDTO> atualizarItem(@PathVariable Long id,
			@RequestBody AtualizarItemRequestDTO dto, @RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		var item = itemService.atualizar(dto, id, false);
		return ResponseEntity.status(HttpStatus.OK).body(item);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletarItem(@PathVariable Long id, @RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		itemService.deletar(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PostMapping("/upload")
	public ResponseEntity<?> uploadCsv(@RequestParam("file") MultipartFile file,
			@RequestHeader("X-ACCESS-TOKEN") String accessToken) throws Exception {
		var resultado = itemService.upload(file);
		return ResponseEntity.ok(resultado);
	}

	@PostMapping("/exportar/{tipo}")
	public ResponseEntity<?> exportar(@PathVariable("tipo") String tipo,
			@RequestHeader("X-ACCESS-TOKEN") String accessToken, @RequestBody ExportarItensRequestDTO dto) {
		return itemService.exportar(dto, tipo);
	}

	@GetMapping("/exportar/todos/{tipo}")
	public ResponseEntity<?> exportarTodos(@PathVariable("tipo") String tipo,
			@RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		return itemService.exportarTodos(tipo);
	}
}

package br.com.fatec.mogi.inventory_service.coreService.web;

import br.com.fatec.mogi.inventory_service.common.web.response.CustomPageResponseDTO;
import br.com.fatec.mogi.inventory_service.coreService.service.LocalizacaoService;
import br.com.fatec.mogi.inventory_service.coreService.web.request.AtualizarLocalizacaoRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarLocalizacaoRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.ConsultarLocalizacaoRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.BuscarLocalizacaoResponseDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.LocalizacaoDetalhadaResponseDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.LocalizacaoResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/core-service/v1/localizacao")
public record LocalizacaoController(LocalizacaoService localizacaoService) {

	@GetMapping
	ResponseEntity<BuscarLocalizacaoResponseDTO> buscar(@RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		var localizacoes = localizacaoService.buscar();
		return ResponseEntity.status(HttpStatus.OK).body(localizacoes);
	}

	@GetMapping("/paginado")
	ResponseEntity<CustomPageResponseDTO<LocalizacaoResponseDTO>> buscarPaginado(
			@ModelAttribute ConsultarLocalizacaoRequestDTO dto, @RequestHeader("X-ACCESS-TOKEN") String accessToken,
			@PageableDefault Pageable pageable) {
		var localizacoes = localizacaoService.buscarPaginado(dto, pageable);
		return ResponseEntity.status(HttpStatus.OK).body(localizacoes);
	}

	@PostMapping
	ResponseEntity<?> cadastrar(@RequestBody CadastrarLocalizacaoRequestDTO dto,
			@RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		localizacaoService.cadastrar(dto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PutMapping("/{id}")
	ResponseEntity<?> atualizar(@RequestBody AtualizarLocalizacaoRequestDTO dto,
			@RequestHeader("X-ACCESS-TOKEN") String accessToken, @PathVariable("id") Long id) {
		localizacaoService.atualizar(dto, id);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("/{id}")
	ResponseEntity<?> deletar(@PathVariable("id") Long id, @RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		localizacaoService.deletar(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping("/detalhada/{id}")
	ResponseEntity<LocalizacaoDetalhadaResponseDTO> buscarDetalhada(@PathVariable("id") Long id,
			@RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		var localizacao = localizacaoService.buscarLocalizacao(id);
		return ResponseEntity.status(HttpStatus.OK).body(localizacao);
	}

}

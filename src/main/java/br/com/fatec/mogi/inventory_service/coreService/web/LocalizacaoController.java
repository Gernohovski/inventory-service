package br.com.fatec.mogi.inventory_service.coreService.web;

import br.com.fatec.mogi.inventory_service.coreService.service.LocalizacaoService;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarLocalizacaoRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.BuscarLocalizacaoResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/core-service/v1/localizacao")
public record LocalizacaoController(LocalizacaoService localizacaoService) {

	@GetMapping
	ResponseEntity<BuscarLocalizacaoResponseDTO> buscar(@RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		var localizacoes = localizacaoService.buscar();
		return ResponseEntity.status(HttpStatus.OK).body(localizacoes);
	}

	@PostMapping
	ResponseEntity<?> cadastrar(@RequestBody CadastrarLocalizacaoRequestDTO dto,
			@RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		localizacaoService.cadastrar(dto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}

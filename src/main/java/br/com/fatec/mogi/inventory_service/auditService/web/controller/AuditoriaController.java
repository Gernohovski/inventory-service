package br.com.fatec.mogi.inventory_service.auditService.web.controller;

import br.com.fatec.mogi.inventory_service.auditService.service.AuditoriaService;
import br.com.fatec.mogi.inventory_service.auditService.web.dto.request.AuditarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.auditService.web.dto.request.ConsultarHistoricoAuditoriaRequestDTO;
import br.com.fatec.mogi.inventory_service.auditService.web.dto.response.AuditoriaAtivaResponseDTO;
import br.com.fatec.mogi.inventory_service.auditService.web.dto.response.AuditoriaHistoricoDetalhadaResponseDTO;
import br.com.fatec.mogi.inventory_service.auditService.web.dto.response.AuditoriaHistoricoResponseDTO;
import br.com.fatec.mogi.inventory_service.common.web.response.CustomPageResponseDTO;
import br.com.fatec.mogi.inventory_service.coreService.service.ItemService;
import br.com.fatec.mogi.inventory_service.coreService.web.request.AtualizarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.ItemResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/audit-service/v1/auditoria")
public record AuditoriaController(AuditoriaService auditoriaService, ItemService itemService) {

	@PostMapping("/iniciar")
	public ResponseEntity<?> iniciarAuditoria(@RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		auditoriaService.iniciarAuditoria();
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PutMapping("/encerrar")
	public ResponseEntity<Void> encerarAuditoria(@RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		auditoriaService.encerrarAuditoria();
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PutMapping("/auditar-item")
	public ResponseEntity<?> auditarItem(@RequestHeader("X-ACCESS-TOKEN") String accessToken,
			@RequestBody AuditarItemRequestDTO dto) {
		auditoriaService.auditarItem(dto);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@GetMapping("/ativa")
	public ResponseEntity<AuditoriaAtivaResponseDTO> auditoriaAtiva(
			@RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		var auditoriaAtiva = auditoriaService.auditoriaAtiva();
		return ResponseEntity.status(HttpStatus.OK).body(auditoriaAtiva);
	}

	@PutMapping("/editar-item/{id}")
	public ResponseEntity<ItemResponseDTO> atualizarItem(@PathVariable Long id,
			@RequestBody AtualizarItemRequestDTO dto, @RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		var item = itemService.atualizar(dto, id);
		return ResponseEntity.status(HttpStatus.OK).body(item);
	}

	@GetMapping("/historico")
	public ResponseEntity<CustomPageResponseDTO<AuditoriaHistoricoResponseDTO>> consultarHistorico(
			@RequestHeader("X-ACCESS-TOKEN") String accessToken,
			@ModelAttribute ConsultarHistoricoAuditoriaRequestDTO dto, @PageableDefault Pageable pageable) {
		var historico = auditoriaService.consultarHistorico(dto, pageable);
		return ResponseEntity.status(HttpStatus.OK).body(historico);
	}

	@GetMapping("/historico/buscar/{codigoAuditoria}")
	public ResponseEntity<AuditoriaHistoricoDetalhadaResponseDTO> buscarHistoricoPorCodigo(
			@RequestHeader("X-ACCESS-TOKEN") String accessToken,
			@PathVariable("codigoAuditoria") String codigoAuditoria) {
		var historico = auditoriaService.buscarHistoricoPorCodigo(codigoAuditoria);
		return ResponseEntity.status(HttpStatus.OK).body(historico);
	}

}

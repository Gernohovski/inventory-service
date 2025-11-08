package br.com.fatec.mogi.inventory_service.auditService.service.impl;

import br.com.fatec.mogi.inventory_service.auditService.domain.exception.*;
import br.com.fatec.mogi.inventory_service.auditService.domain.model.Auditoria;
import br.com.fatec.mogi.inventory_service.auditService.domain.model.ItemAuditado;
import br.com.fatec.mogi.inventory_service.auditService.domain.model.ItemAuditadoHistorico;
import br.com.fatec.mogi.inventory_service.auditService.domain.model.mapper.ItemAuditadoMapper;
import br.com.fatec.mogi.inventory_service.auditService.repository.AuditoriaHistoricoRepository;
import br.com.fatec.mogi.inventory_service.auditService.repository.AuditoriaRepository;
import br.com.fatec.mogi.inventory_service.auditService.repository.ItemAuditadoRepository;
import br.com.fatec.mogi.inventory_service.auditService.service.AuditoriaHistoricoService;
import br.com.fatec.mogi.inventory_service.auditService.service.AuditoriaService;
import br.com.fatec.mogi.inventory_service.auditService.web.dto.request.AuditarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.auditService.web.dto.request.ConsultarHistoricoAuditoriaRequestDTO;
import br.com.fatec.mogi.inventory_service.auditService.web.dto.response.AuditoriaAtivaResponseDTO;
import br.com.fatec.mogi.inventory_service.auditService.web.dto.response.AuditoriaHistoricoDetalhadaResponseDTO;
import br.com.fatec.mogi.inventory_service.auditService.web.dto.response.AuditoriaHistoricoResponseDTO;
import br.com.fatec.mogi.inventory_service.auditService.web.dto.response.ItensAuditadosAuditoriaAtivaResponseDTO;
import br.com.fatec.mogi.inventory_service.common.web.context.RequestContext;
import br.com.fatec.mogi.inventory_service.common.web.response.CustomPageResponseDTO;
import br.com.fatec.mogi.inventory_service.coreService.domain.exception.ItemNaoEncontradoException;
import br.com.fatec.mogi.inventory_service.coreService.repository.ItemRepository;
import br.com.fatec.mogi.inventory_service.coreService.service.ItemService;
import br.com.fatec.mogi.inventory_service.coreService.web.request.AtualizarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.ItemResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditoriaServiceImpl implements AuditoriaService {

	private final ItemRepository itemRepository;

	private final AuditoriaRepository auditoriaRepository;

	private final ItemAuditadoRepository itemAuditadoRepository;

	private final AuditoriaHistoricoService auditoriaHistoricoService;

	private final AuditoriaHistoricoRepository auditoriaHistoricoRepository;

	private final ItemService itemService;

	private final ItemAuditadoMapper itemAuditadoMapper;

	@Override
	@Transactional
	public void iniciarAuditoria() {
		if (auditoriaRepository.findAtiva().isPresent()) {
			throw new AuditoriaJaAcontecendoException();
		}
		var itens = itemRepository.findAll();
		if (itens.isEmpty()) {
			throw new NaoHaItensException();
		}
		Auditoria auditoria = Auditoria.builder()
			.codigoAuditoria(auditoriaHistoricoService.gerarCodigoAuditoria())
			.dataInicio(LocalDateTime.now())
			.usuarioResponsavel(RequestContext.getUsuario())
			.build();
		Auditoria auditoriaSalva = auditoriaRepository.save(auditoria);
		List<ItemAuditado> itensAuditados = new ArrayList<>();
		itens.forEach(item -> {
			ItemAuditado itemAuditado = ItemAuditado.builder()
				.auditoria(auditoriaSalva)
				.item(item)
				.conformidade(false)
				.localizado(false)
				.build();
			itensAuditados.add(itemAuditado);
		});
		itemAuditadoRepository.saveAll(itensAuditados);
		auditoriaSalva.setItensAuditados(itensAuditados);
	}

	@Override
	@Transactional
	public void auditarItem(AuditarItemRequestDTO dto) {
		auditoriaRepository.findAtiva().orElseThrow(NaoHaAuditoriaAtivaException::new);
		var item = itemRepository.findByCodigoItem(dto.getCodigoItem()).orElseThrow(ItemNaoEncontradoException::new);
		item.setUltimaVezAuditado(LocalDateTime.now());
		itemRepository.save(item);
		var itemAuditado = itemAuditadoRepository.findByItemId(item.getId())
			.orElseThrow(ItemNaoSendoAuditadoException::new);
		itemAuditado.setConformidade(true);
		itemAuditado.setLocalizado(true);
		itemAuditado.setUsuarioResponsavel(RequestContext.getUsuario());
		itemAuditado.setObservacao(dto.getObservacao());
		itemAuditado.setDataVerificacao(LocalDateTime.now());
		itemAuditadoRepository.save(itemAuditado);
	}

	@Override
	public AuditoriaAtivaResponseDTO auditoriaAtiva() {
		var auditoriaAtiva = auditoriaRepository.findAtiva().orElseThrow(NaoHaAuditoriaAtivaException::new);
		long totalItens = auditoriaAtiva.getItensAuditados().size();
		long quantidadeAuditado = auditoriaAtiva.getItensAuditados()
			.stream()
			.filter(ItemAuditado::getLocalizado)
			.count();
		long quantidadeNaoAuditado = totalItens - quantidadeAuditado;
		BigDecimal porcentagem = totalItens == 0 ? BigDecimal.ZERO
				: BigDecimal.valueOf(quantidadeAuditado * 100.0 / totalItens).setScale(2, RoundingMode.HALF_UP);
		return AuditoriaAtivaResponseDTO.builder()
			.codigoAuditoria(auditoriaAtiva.getCodigoAuditoria())
			.porcentagem(porcentagem)
			.quantidadeAuditados(quantidadeAuditado)
			.quantidadeNaoAuditados(quantidadeNaoAuditado)
			.build();
	}

	@Override
	@Transactional
	public void encerrarAuditoria() {
		var auditoriaAtiva = auditoriaRepository.findAtiva().orElseThrow(NaoHaAuditoriaAtivaException::new);
		auditoriaAtiva.setDataFim(LocalDateTime.now());
		auditoriaRepository.save(auditoriaAtiva);
		auditoriaHistoricoService.arquivarAuditoria(auditoriaAtiva);
		itemAuditadoRepository.deleteAll(auditoriaAtiva.getItensAuditados());
		auditoriaRepository.delete(auditoriaAtiva);
	}

	@Override
	public CustomPageResponseDTO<AuditoriaHistoricoResponseDTO> consultarHistorico(
			ConsultarHistoricoAuditoriaRequestDTO dto, Pageable pageable) {
		var page = auditoriaHistoricoRepository.findHistorico(dto.getDataInicio(), dto.getDataFim(), pageable);
		var content = page.getContent()
			.stream()
			.map(historico -> AuditoriaHistoricoResponseDTO.builder()
				.id(historico.getId())
				.codigoAuditoria(historico.getCodigoAuditoria())
				.dataInicio(historico.getDataInicio())
				.dataFim(historico.getDataFim())
				.usuarioResponsavelNome(historico.getUsuarioResponsavelNome())
				.totalItens(historico.getTotalItens())
				.build())
			.toList();
		return CustomPageResponseDTO.<AuditoriaHistoricoResponseDTO>builder()
			.content(content)
			.page(page.getNumber())
			.size(page.getSize())
			.totalElements(page.getTotalElements())
			.totalPages(page.getTotalPages())
			.build();
	}

	@Override
	@Transactional
	public AuditoriaHistoricoDetalhadaResponseDTO buscarHistoricoPorCodigo(String codigoAuditoria) {
		var historico = auditoriaHistoricoRepository.findByCodigoAuditoria(codigoAuditoria)
			.orElseThrow(AuditoriaNaoEncontradaException::new);
		var itensHistorico = historico.getItensAuditadosHistorico();
		if (itensHistorico == null) {
			itensHistorico = new ArrayList<>();
		}
		var itensLocalizados = itensHistorico.stream()
			.filter(item -> item.getLocalizado() != null && item.getLocalizado())
			.toList();
		var itensNaoLocalizados = itensHistorico.stream()
			.filter(item -> item.getLocalizado() == null || !item.getLocalizado())
			.toList();
		return AuditoriaHistoricoDetalhadaResponseDTO.builder()
			.id(historico.getId())
			.codigoAuditoria(historico.getCodigoAuditoria())
			.dataInicio(historico.getDataInicio())
			.dataFim(historico.getDataFim())
			.usuarioResponsavelNome(historico.getUsuarioResponsavelNome())
			.totalItens(historico.getTotalItens())
			.itensLocalizados(itensLocalizados)
			.itensNaoLocalizados(itensNaoLocalizados)
			.build();
	}

	@Override
	public ItemResponseDTO atualizar(AtualizarItemRequestDTO dto, Long id) {
		var itemAuditado = itemAuditadoRepository.findByItemId(id).orElseThrow(ItemNaoSendoAuditadoException::new);
		var itemAtualizado = itemService.atualizar(dto, id, true);
		itemAuditado.setConformidade(true);
		itemAuditado.setLocalizado(true);
		itemAuditado.setUsuarioResponsavel(RequestContext.getUsuario());
		itemAuditado.setDataVerificacao(LocalDateTime.now());
		return itemAtualizado;
	}

	@Override
	public ItensAuditadosAuditoriaAtivaResponseDTO itensNaoLocalizadosAuditoriaAtiva() {
		var auditoriaAtiva = auditoriaRepository.findAtiva().orElseThrow(NaoHaAuditoriaAtivaException::new);
		var itensNaoLocalizados = auditoriaAtiva.getItensAuditados()
			.stream()
			.filter(item -> !item.getLocalizado())
			.toList();
		var itensLocalizados = auditoriaAtiva.getItensAuditados().stream().filter(ItemAuditado::getLocalizado).toList();
		return ItensAuditadosAuditoriaAtivaResponseDTO.builder()
			.codigoAuditoria(auditoriaAtiva.getCodigoAuditoria())
			.itensLocalizados(itemAuditadoMapper.from(itensLocalizados))
			.itensNaoLocalizados(itemAuditadoMapper.from(itensNaoLocalizados))
			.build();
	}

}

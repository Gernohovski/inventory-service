package br.com.fatec.mogi.inventory_service.coreService.service.impl;

import br.com.fatec.mogi.inventory_service.authService.service.AutorizacaoService;
import br.com.fatec.mogi.inventory_service.common.web.response.CustomPageResponseDTO;
import br.com.fatec.mogi.inventory_service.coreService.domain.exception.*;
import br.com.fatec.mogi.inventory_service.coreService.domain.model.Item;
import br.com.fatec.mogi.inventory_service.coreService.repository.*;
import br.com.fatec.mogi.inventory_service.coreService.service.ItemService;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.ConsultarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.ItemResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

	private final AutorizacaoService autorizacaoService;

	private final ItemRepository itemRepository;

	private final CategoriaItemRepository categoriaItemRepository;

	private final TipoEntradaRepository tipoEntradaRepository;

	private final StatusItemRepository statusItemRepository;

	private final LocalizacaoRepository localizacaoRepository;

	@Override
	public void cadastrarItem(CadastrarItemRequestDTO dto) {
		if (itemRepository.existsByCodigoItem(dto.getCodigoItem())) {
			throw new ItemJaCadastradoException();
		}
		var categoria = categoriaItemRepository.findById(dto.getCategoriaItemId())
			.orElseThrow(CategoriaNaoEncontradaException::new);
		var tipoEntrada = tipoEntradaRepository.findById(dto.getTipoEntradaId())
			.orElseThrow(TipoEntradaNaoEncontradaException::new);
		var statusItem = statusItemRepository.findById(dto.getStatusItemId())
			.orElseThrow(StatusItemNaoEncontradoException::new);
		var localizacao = localizacaoRepository.findById(dto.getLocalizacaoId())
			.orElseThrow(LocalizacaoNaoEncontradaException::new);
		var item = Item.builder()
			.nomeItem(dto.getNomeItem())
			.codigoItem(dto.getCodigoItem())
			.descricaoCurta(dto.getDescricaoCurta())
			.descricaoDetalhada(dto.getDescricaoDetalhada())
			.numeroSerie(dto.getNumeroSerie())
			.notaFiscal(dto.getNotaFiscal())
			.categoriaItem(categoria)
			.tipoEntrada(tipoEntrada)
			.statusItem(statusItem)
			.localizacao(localizacao)
			.dataCadastro(LocalDateTime.now())
			.dataAlteracao(LocalDateTime.now())
			.build();
		itemRepository.save(item);
	}

	@Override
	public CustomPageResponseDTO<ItemResponseDTO> filtrarItems(ConsultarItemRequestDTO dto, Pageable pageable) {
		var pagina = itemRepository.filtrar(dto.getDataCadastroInicio(), dto.getDataCadastroFim(),
				dto.getCategoriaItemId(), dto.getLocalizacaoId(), dto.getStatusItemId(), dto.getTipoEntradaId(),
				dto.getNomeItem(), dto.getCodigoItem(), dto.getNumeroSerie(), dto.getNotaFiscal(), pageable);
		return CustomPageResponseDTO.<ItemResponseDTO>builder()
			.content(pagina.getContent())
			.size(pagina.getSize())
			.page(pagina.getNumber())
			.totalElements(pagina.getTotalElements())
			.totalPages(pagina.getTotalPages())
			.build();
	}

}

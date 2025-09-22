package br.com.fatec.mogi.inventory_service.coreService.service.impl;

import br.com.fatec.mogi.inventory_service.common.web.response.CustomPageResponseDTO;
import br.com.fatec.mogi.inventory_service.coreService.domain.exception.*;
import br.com.fatec.mogi.inventory_service.coreService.domain.mapper.ItemMapper;
import br.com.fatec.mogi.inventory_service.coreService.domain.model.Item;
import br.com.fatec.mogi.inventory_service.coreService.repository.*;
import br.com.fatec.mogi.inventory_service.coreService.service.ItemService;
import br.com.fatec.mogi.inventory_service.coreService.web.request.AtualizarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.ConsultarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.ItemResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

	private final ItemRepository itemRepository;

	private final CategoriaItemRepository categoriaItemRepository;

	private final TipoEntradaRepository tipoEntradaRepository;

	private final StatusItemRepository statusItemRepository;

	private final LocalizacaoRepository localizacaoRepository;

	private final ItemMapper itemMapper;

	@Override
	public ItemResponseDTO cadastrarItem(CadastrarItemRequestDTO dto) {
		if (itemRepository.existsByCodigoItem(dto.getCodigoItem())) {
			throw new ItemJaCadastradoException();
		}
		categoriaItemRepository.findById(dto.getCategoriaItemId())
			.orElseThrow(CategoriaNaoEncontradaException::new);
		tipoEntradaRepository.findById(dto.getTipoEntradaId())
			.orElseThrow(TipoEntradaNaoEncontradaException::new);
		statusItemRepository.findById(dto.getStatusItemId())
			.orElseThrow(StatusItemNaoEncontradoException::new);
		localizacaoRepository.findById(dto.getLocalizacaoId())
			.orElseThrow(LocalizacaoNaoEncontradaException::new);
		Item item = itemMapper.from(dto);
		item.setDataAlteracao(LocalDateTime.now());
		item.setDataCadastro(LocalDateTime.now());
		return itemMapper.from(itemRepository.save(item));
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

	@Override
	public ItemResponseDTO atualizar(AtualizarItemRequestDTO dto, Long id) {
		var item = itemRepository.findById(id).orElseThrow(ItemNaoEncontradoException::new);
		var codigoItemSalvo = item.getCodigoItem();
		if (dto.getCategoriaItemId() != null) {
			var categoria = categoriaItemRepository.findById(dto.getCategoriaItemId())
					.orElseThrow(CategoriaNaoEncontradaException::new);
			item.setCategoriaItem(categoria);
		}
		if (dto.getTipoEntradaId() != null) {
			var tipoEntrada = tipoEntradaRepository.findById(dto.getTipoEntradaId())
					.orElseThrow(TipoEntradaNaoEncontradaException::new);
			item.setTipoEntrada(tipoEntrada);
		}
		if (dto.getStatusItemId() != null) {
			var status = statusItemRepository.findById(dto.getStatusItemId())
					.orElseThrow(StatusItemNaoEncontradoException::new);
			item.setStatusItem(status);
		}
		if (dto.getLocalizacaoId() != null) {
			var localizacao = localizacaoRepository.findById(dto.getLocalizacaoId())
					.orElseThrow(LocalizacaoNaoEncontradaException::new);
			item.setLocalizacao(localizacao);
		}
		itemMapper.update(dto ,item);
		if (!Objects.equals(item.getCodigoItem(), codigoItemSalvo)) {
			if (itemRepository.existsByCodigoItem(item.getCodigoItem())) {
				throw new ItemJaCadastradoException();
			}
		}
		item.setDataAlteracao(LocalDateTime.now());
		return itemMapper.from(itemRepository.save(item));
	}

}

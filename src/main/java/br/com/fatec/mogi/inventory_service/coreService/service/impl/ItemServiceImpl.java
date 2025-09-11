package br.com.fatec.mogi.inventory_service.coreService.service.impl;

import br.com.fatec.mogi.inventory_service.authService.service.AutorizacaoService;
import br.com.fatec.mogi.inventory_service.authService.web.dto.request.AutorizarUsuarioRequestDTO;
import br.com.fatec.mogi.inventory_service.common.domain.enums.FuncionalidadesEnum;
import br.com.fatec.mogi.inventory_service.coreService.domain.exception.CategoriaNaoEncontradaException;
import br.com.fatec.mogi.inventory_service.coreService.domain.exception.ItemJaCadastradoException;
import br.com.fatec.mogi.inventory_service.coreService.domain.exception.LocalizacaoNaoEncontradaException;
import br.com.fatec.mogi.inventory_service.coreService.domain.exception.StatusItemNaoEncontradoException;
import br.com.fatec.mogi.inventory_service.coreService.domain.exception.TipoEntradaNaoEncontradaException;
import br.com.fatec.mogi.inventory_service.coreService.domain.model.Item;
import br.com.fatec.mogi.inventory_service.coreService.repository.CategoriaItemRepository;
import br.com.fatec.mogi.inventory_service.coreService.repository.ItemRepository;
import br.com.fatec.mogi.inventory_service.coreService.repository.LocalizacaoRepository;
import br.com.fatec.mogi.inventory_service.coreService.repository.StatusItemRepository;
import br.com.fatec.mogi.inventory_service.coreService.repository.TipoEntradaRepository;
import br.com.fatec.mogi.inventory_service.coreService.service.ItemService;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarItemRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
	public void cadastrarItem(CadastrarItemRequestDTO dto, String accessToken) {
		AutorizarUsuarioRequestDTO autorizarUsuarioRequestDTO = AutorizarUsuarioRequestDTO.builder()
			.funcionalidade(FuncionalidadesEnum.CADASTRO_ITEM.toString())
			.build();
		autorizacaoService.autorizar(autorizarUsuarioRequestDTO, accessToken);
		if (!itemRepository.existsByCodigoItem(dto.getCodigoItem())) {
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
			.build();
		itemRepository.save(item);
	}

}

package br.com.fatec.mogi.inventory_service.coreService.service.impl;

import br.com.fatec.mogi.inventory_service.coreService.domain.exception.StatusItemJaCadastradoException;
import br.com.fatec.mogi.inventory_service.coreService.domain.exception.StatusItemNaoEncontradoException;
import br.com.fatec.mogi.inventory_service.coreService.domain.exception.StatusItemNaoPodeSerExcluidoException;
import br.com.fatec.mogi.inventory_service.coreService.domain.model.StatusItem;
import br.com.fatec.mogi.inventory_service.coreService.repository.ItemRepository;
import br.com.fatec.mogi.inventory_service.coreService.repository.StatusItemRepository;
import br.com.fatec.mogi.inventory_service.coreService.service.StatusItemService;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarStatusItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.BuscarStatusItemResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatusItemServiceImpl implements StatusItemService {

	private final StatusItemRepository statusItemRepository;

	private final ItemRepository itemRepository;

	@Override
	public StatusItem cadastrar(CadastrarStatusItemRequestDTO dto) {
		if (statusItemRepository.existsByNome(dto.getNome())) {
			throw new StatusItemJaCadastradoException();
		}
		StatusItem statusItem = StatusItem.builder().nome(dto.getNome()).build();
		return statusItemRepository.save(statusItem);
	}

	@Override
	public void deletar(Long id) {
		statusItemRepository.findById(id).orElseThrow(StatusItemNaoEncontradoException::new);
		if (itemRepository.existsByStatusItemId(id)) {
			throw new StatusItemNaoPodeSerExcluidoException();
		}
		statusItemRepository.deleteById(id);
	}

	@Override
	public BuscarStatusItemResponseDTO listar() {
		return new BuscarStatusItemResponseDTO(statusItemRepository.findAll());
	}

}

package br.com.fatec.mogi.inventory_service.coreService.service.impl;

import br.com.fatec.mogi.inventory_service.coreService.domain.exception.StatusItemJaCadastradoException;
import br.com.fatec.mogi.inventory_service.coreService.domain.exception.StatusItemNaoEncontradoException;
import br.com.fatec.mogi.inventory_service.coreService.domain.model.StatusItem;
import br.com.fatec.mogi.inventory_service.coreService.repository.StatusItemRepository;
import br.com.fatec.mogi.inventory_service.coreService.service.StatusItemService;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarStatusItemRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatusItemServiceImpl implements StatusItemService {

	private final StatusItemRepository statusItemRepository;

	@Override
	public void cadastrar(CadastrarStatusItemRequestDTO dto) {
		if (statusItemRepository.existsByNome(dto.getNome())) {
			throw new StatusItemJaCadastradoException();
		}
		StatusItem statusItem = StatusItem.builder().nome(dto.getNome()).build();
		statusItemRepository.save(statusItem);
	}

	@Override
	public void deletar(Long id) {
		statusItemRepository.findById(id).orElseThrow(StatusItemNaoEncontradoException::new);
		statusItemRepository.deleteById(id);
	}

}

package br.com.fatec.mogi.inventory_service.coreService.service.impl;

import br.com.fatec.mogi.inventory_service.coreService.domain.exception.CategoriaJaCadastradaException;
import br.com.fatec.mogi.inventory_service.coreService.domain.exception.CategoriaNaoEncontradaException;
import br.com.fatec.mogi.inventory_service.coreService.domain.model.CategoriaItem;
import br.com.fatec.mogi.inventory_service.coreService.repository.CategoriaItemRepository;
import br.com.fatec.mogi.inventory_service.coreService.service.CategoriaItemService;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarCategoriaItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.BuscarCategoriasResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoriaItemServiceImpl implements CategoriaItemService {

	private final CategoriaItemRepository categoriaItemRepository;

	@Override
	public BuscarCategoriasResponseDTO buscar() {
		return new BuscarCategoriasResponseDTO(categoriaItemRepository.findAll());
	}

	@Override
	public void cadastrar(CadastrarCategoriaItemRequestDTO dto) {
		if (categoriaItemRepository.existsByNome(dto.getNome())) {
			throw new CategoriaJaCadastradaException(dto.getNome());
		}
		CategoriaItem categoriaSalvar = CategoriaItem.builder().nome(dto.getNome()).build();
		categoriaItemRepository.save(categoriaSalvar);
	}

	@Override
	public void deletar(Long id) {
		categoriaItemRepository.findById(id).orElseThrow(CategoriaNaoEncontradaException::new);
		categoriaItemRepository.deleteById(id);
	}

}

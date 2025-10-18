package br.com.fatec.mogi.inventory_service.coreService.strategy.validarItem.impl;

import br.com.fatec.mogi.inventory_service.coreService.domain.model.CategoriaItem;
import br.com.fatec.mogi.inventory_service.coreService.repository.CategoriaItemRepository;
import br.com.fatec.mogi.inventory_service.coreService.strategy.validarItem.ValidarItemContexto;
import br.com.fatec.mogi.inventory_service.coreService.strategy.validarItem.ValidarItemStrategy;
import br.com.fatec.mogi.inventory_service.coreService.web.request.ItemUploadRequestDTO;
import jakarta.annotation.Priority;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Priority(4)
@Component
@RequiredArgsConstructor
public class ValidarCategoriaStrategy implements ValidarItemStrategy {

	private final CategoriaItemRepository categoriaItemRepository;

	@Override
	public void executar(ItemUploadRequestDTO dto, ValidarItemContexto contexto) {
		contexto.adicionarStrategieExecutada(this);
		if (dto.getCategoria() == null || dto.getCategoria().trim().isEmpty()) {
			contexto.adicionarErro("Categoria inválida", dto.getNumeroLinha().toString());
			contexto.setEncerrarFluxo(true);
		}
		var categoria = categoriaItemRepository.findByNome(dto.getCategoria());
		if (categoria.isEmpty()) {
			CategoriaItem categoriaNova = CategoriaItem.builder().nome(dto.getCategoria()).build();
			var categoriaSalva = categoriaItemRepository.save(categoriaNova);
			contexto.getItem().setCategoriaItem(categoriaSalva);
			return;
		}
		contexto.getItem().setCategoriaItem(categoria.get());
	}

}

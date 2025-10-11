package br.com.fatec.mogi.inventory_service.coreService.strategy.impl;

import br.com.fatec.mogi.inventory_service.coreService.repository.CategoriaItemRepository;
import br.com.fatec.mogi.inventory_service.coreService.strategy.ValidarItemContexto;
import br.com.fatec.mogi.inventory_service.coreService.strategy.ValidarItemStrategy;
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
			contexto.adicionarErro("Categoria não mapeada", dto.getNumeroLinha().toString());
			contexto.setEncerrarFluxo(true);
			return;
		}
		contexto.getItem().setCategoriaItem(categoria.get());
	}

}

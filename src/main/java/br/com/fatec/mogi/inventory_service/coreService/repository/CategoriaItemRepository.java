package br.com.fatec.mogi.inventory_service.coreService.repository;

import br.com.fatec.mogi.inventory_service.coreService.domain.model.CategoriaItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaItemRepository extends JpaRepository<CategoriaItem, Long> {

	boolean existsByNome(String nome);

}

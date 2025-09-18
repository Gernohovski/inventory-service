package br.com.fatec.mogi.inventory_service.coreService.repository;

import br.com.fatec.mogi.inventory_service.coreService.domain.model.CategoriaItem;
import br.com.fatec.mogi.inventory_service.coreService.web.response.CategoriaItemResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoriaItemRepository extends JpaRepository<CategoriaItem, Long> {

	boolean existsByNome(String nome);

	@Query("""
			SELECT NEW br.com.fatec.mogi.inventory_service.coreService.web.response.CategoriaItemResponseDTO(
				c.id,
				c.nome
			)
			FROM CategoriaItem c
			""")
	Page<CategoriaItemResponseDTO> findPaginado(Pageable pageable);

}

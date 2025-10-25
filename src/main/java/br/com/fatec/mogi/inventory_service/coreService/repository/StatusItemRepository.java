package br.com.fatec.mogi.inventory_service.coreService.repository;

import br.com.fatec.mogi.inventory_service.coreService.domain.model.StatusItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusItemRepository extends JpaRepository<StatusItem, Long> {

	Optional<StatusItem> findByNome(String nome);

	boolean existsByNome(String nome);

}

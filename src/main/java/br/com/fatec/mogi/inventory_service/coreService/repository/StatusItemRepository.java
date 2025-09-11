package br.com.fatec.mogi.inventory_service.coreService.repository;

import br.com.fatec.mogi.inventory_service.coreService.domain.model.StatusItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusItemRepository extends JpaRepository<StatusItem, Long> {

}

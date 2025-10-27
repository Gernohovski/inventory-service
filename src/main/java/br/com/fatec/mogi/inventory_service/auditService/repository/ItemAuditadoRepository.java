package br.com.fatec.mogi.inventory_service.auditService.repository;

import br.com.fatec.mogi.inventory_service.auditService.domain.model.ItemAuditado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemAuditadoRepository extends JpaRepository<ItemAuditado, Long> {

	Optional<ItemAuditado> findByItemId(Long itemId);

}

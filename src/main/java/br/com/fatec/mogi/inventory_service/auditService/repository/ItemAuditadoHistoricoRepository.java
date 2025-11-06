package br.com.fatec.mogi.inventory_service.auditService.repository;

import br.com.fatec.mogi.inventory_service.auditService.domain.model.ItemAuditadoHistorico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemAuditadoHistoricoRepository extends JpaRepository<ItemAuditadoHistorico, Long> {

}

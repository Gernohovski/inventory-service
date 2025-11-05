package br.com.fatec.mogi.inventory_service.auditService.repository;

import br.com.fatec.mogi.inventory_service.auditService.domain.model.AuditoriaHistorico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AuditoriaHistoricoRepository extends JpaRepository<AuditoriaHistorico, Long> {

	Optional<AuditoriaHistorico> findByCodigoAuditoria(String codigoAuditoria);

	@Query("""
			SELECT ah FROM AuditoriaHistorico ah
			WHERE 1=1
			  AND (:dataInicio IS NULL OR ah.dataInicio >= :dataInicio)
			  AND (:dataFim IS NULL OR ah.dataFim <= :dataFim)
			ORDER BY ah.dataInicio DESC
			""")
	Page<AuditoriaHistorico> findHistorico(@Param("dataInicio") LocalDateTime dataInicio,
			@Param("dataFim") LocalDateTime dataFim, Pageable pageable);

	@Query("""
			SELECT COUNT(ah) FROM AuditoriaHistorico ah
			WHERE YEAR(ah.dataInicio) = :ano
			""")
	Long countByAno(@Param("ano") int ano);

}

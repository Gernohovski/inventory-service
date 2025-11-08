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

	@Query("SELECT ah FROM AuditoriaHistorico ah LEFT JOIN FETCH ah.itensAuditadosHistorico WHERE ah.codigoAuditoria = :codigoAuditoria")
	Optional<AuditoriaHistorico> findByCodigoAuditoria(@Param("codigoAuditoria") String codigoAuditoria);

	@Query("""
			SELECT ah FROM AuditoriaHistorico ah
			WHERE ah.dataInicio >= COALESCE(:dataInicio, ah.dataInicio)
			  AND ah.dataFim <= COALESCE(:dataFim, ah.dataFim)
			order BY ah.dataInicio DESC
			""")
	Page<AuditoriaHistorico> findHistorico(@Param("dataInicio") LocalDateTime dataInicio,
			@Param("dataFim") LocalDateTime dataFim, Pageable pageable);

	@Query("""
			SELECT COUNT(ah) FROM AuditoriaHistorico ah
			WHERE YEAR(ah.dataInicio) = :ano
			""")
	Long countByAno(@Param("ano") int ano);

}

package br.com.fatec.mogi.inventory_service.auditService.repository;

import br.com.fatec.mogi.inventory_service.auditService.domain.model.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {

	@Query("""
			SELECT a FROM Auditoria a WHERE a.dataFim IS NULL
			""")
	Optional<Auditoria> findAtiva();

}

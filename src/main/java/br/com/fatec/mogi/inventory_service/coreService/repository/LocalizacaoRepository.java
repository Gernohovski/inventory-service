package br.com.fatec.mogi.inventory_service.coreService.repository;

import br.com.fatec.mogi.inventory_service.coreService.domain.model.Localizacao;
import br.com.fatec.mogi.inventory_service.coreService.web.response.LocalizacaoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LocalizacaoRepository extends JpaRepository<Localizacao, Long> {

	@Query(value = """
			SELECT NEW br.com.fatec.mogi.inventory_service.coreService.web.response.LocalizacaoResponseDTO(
			    l.id,
			    l.nomeSala,
			    l.andar
			)
			FROM Localizacao l
			""")
	Page<LocalizacaoResponseDTO> findPaginado(Pageable pageable);

}

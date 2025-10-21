package br.com.fatec.mogi.inventory_service.coreService.repository;

import br.com.fatec.mogi.inventory_service.coreService.domain.model.Localizacao;
import br.com.fatec.mogi.inventory_service.coreService.web.request.ConsultarLocalizacaoRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.LocalizacaoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LocalizacaoRepository extends JpaRepository<Localizacao, Long> {

	@Query(value = """
			SELECT NEW br.com.fatec.mogi.inventory_service.coreService.web.response.LocalizacaoResponseDTO(
			    l.id,
			    l.nomeSala,
			    l.andar,
			    l.quantidadeItens
			)
			FROM Localizacao l
			WHERE (:dto.termoPesquisa IS NULL OR
			      UPPER(l.nomeSala) LIKE CONCAT('%', UPPER(TRIM(:dto.termoPesquisa)), '%') OR
			      UPPER(CAST(l.andar AS string)) LIKE CONCAT('%', UPPER(TRIM(:dto.termoPesquisa)), '%'))
			""")
	Page<LocalizacaoResponseDTO> findPaginado(@Param("dto") ConsultarLocalizacaoRequestDTO dto, Pageable pageable);

	Optional<Localizacao> findByNomeSala(String nomeSala);

}

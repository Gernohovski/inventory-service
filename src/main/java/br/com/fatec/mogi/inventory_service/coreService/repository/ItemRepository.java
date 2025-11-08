package br.com.fatec.mogi.inventory_service.coreService.repository;

import br.com.fatec.mogi.inventory_service.coreService.domain.model.Item;
import br.com.fatec.mogi.inventory_service.coreService.web.response.ItemPorCategoriaResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

	boolean existsByCodigoItem(String codigo);

	@Query(value = """
			SELECT i.*
			FROM item i
			LEFT JOIN localizacao l ON l.id = i.localizacao_id
			LEFT JOIN categoria_item c ON c.id = i.categoria_item_id
			LEFT JOIN status_item s ON s.id = i.status_item_id
			WHERE 1=1
			  AND (CAST(:dataCadastroInicio AS timestamp) IS NULL OR i.data_cadastro >= CAST(:dataCadastroInicio AS timestamp))
			  AND (CAST(:dataCadastroFim AS timestamp) IS NULL OR i.data_cadastro <= CAST(:dataCadastroFim AS timestamp))
			  AND (CAST(:categoriaItemId AS bigint) IS NULL OR i.categoria_item_id = CAST(:categoriaItemId AS bigint))
			  AND (CAST(:localizacaoId AS bigint) IS NULL OR i.localizacao_id = CAST(:localizacaoId AS bigint))
			  AND (CAST(:statusItemId AS bigint) IS NULL OR i.status_item_id = CAST(:statusItemId AS bigint))
			  AND (CAST(:nomeItem AS varchar) IS NULL OR CAST(:nomeItem AS varchar) = '' OR upper(i.nome_item) LIKE upper('%' || CAST(:nomeItem AS varchar) || '%'))
			  AND (CAST(:codigoItem AS varchar) IS NULL OR CAST(:codigoItem AS varchar) = '' OR upper(i.codigo_item) LIKE upper('%' || CAST(:codigoItem AS varchar) || '%'))
			  AND (CAST(:numeroSerie AS varchar) IS NULL OR CAST(:numeroSerie AS varchar) = '' OR upper(i.numero_serie) LIKE upper('%' || CAST(:numeroSerie AS varchar) || '%'))
			  AND (CAST(:notaFiscal AS varchar) IS NULL OR CAST(:notaFiscal AS varchar) = '' OR upper(i.nota_fiscal) LIKE upper('%' || CAST(:notaFiscal AS varchar) || '%'))
			  AND (
			    CAST(:termoPesquisa AS varchar) IS NULL OR CAST(:termoPesquisa AS varchar) = '' OR
			    upper(
			      COALESCE(i.codigo_item, '') || ' ' ||
			      COALESCE(i.numero_serie, '') || ' ' ||
			      COALESCE(to_char(i.data_cadastro, 'DD/MM/YYYY'), '') || ' ' ||
			      COALESCE(l.nome_sala, '') || ' ' ||
			      COALESCE(c.nome, '') || ' ' ||
			      COALESCE(s.nome, '')
			    ) LIKE upper('%' || CAST(:termoPesquisa AS varchar) || '%')
			  )
			ORDER BY i.data_cadastro DESC
			""",
			countQuery = """
					SELECT COUNT(*)
					FROM item i
					LEFT JOIN localizacao l ON l.id = i.localizacao_id
					LEFT JOIN categoria_item c ON c.id = i.categoria_item_id
					LEFT JOIN status_item s ON s.id = i.status_item_id
					WHERE 1=1
					  AND (CAST(:dataCadastroInicio AS timestamp) IS NULL OR i.data_cadastro >= CAST(:dataCadastroInicio AS timestamp))
					  AND (CAST(:dataCadastroFim AS timestamp) IS NULL OR i.data_cadastro <= CAST(:dataCadastroFim AS timestamp))
					  AND (CAST(:categoriaItemId AS bigint) IS NULL OR i.categoria_item_id = CAST(:categoriaItemId AS bigint))
					  AND (CAST(:localizacaoId AS bigint) IS NULL OR i.localizacao_id = CAST(:localizacaoId AS bigint))
					  AND (CAST(:statusItemId AS bigint) IS NULL OR i.status_item_id = CAST(:statusItemId AS bigint))
					  AND (CAST(:nomeItem AS varchar) IS NULL OR CAST(:nomeItem AS varchar) = '' OR upper(i.nome_item) LIKE upper('%' || CAST(:nomeItem AS varchar) || '%'))
					  AND (CAST(:codigoItem AS varchar) IS NULL OR CAST(:codigoItem AS varchar) = '' OR upper(i.codigo_item) LIKE upper('%' || CAST(:codigoItem AS varchar) || '%'))
					  AND (CAST(:numeroSerie AS varchar) IS NULL OR CAST(:numeroSerie AS varchar) = '' OR upper(i.numero_serie) LIKE upper('%' || CAST(:numeroSerie AS varchar) || '%'))
					  AND (CAST(:notaFiscal AS varchar) IS NULL OR CAST(:notaFiscal AS varchar) = '' OR upper(i.nota_fiscal) LIKE upper('%' || CAST(:notaFiscal AS varchar) || '%'))
					  AND (
					    CAST(:termoPesquisa AS varchar) IS NULL OR CAST(:termoPesquisa AS varchar) = '' OR
					    upper(
					      COALESCE(i.codigo_item, '') || ' ' ||
					      COALESCE(i.numero_serie, '') || ' ' ||
					      COALESCE(to_char(i.data_cadastro, 'DD/MM/YYYY'), '') || ' ' ||
					      COALESCE(l.nome_sala, '') || ' ' ||
					      COALESCE(c.nome, '') || ' ' ||
					      COALESCE(s.nome, '')
					    ) LIKE upper('%' || CAST(:termoPesquisa AS varchar) || '%')
					  )
					""",
			nativeQuery = true)
	Page<Item> filtrar(@Param("dataCadastroInicio") java.time.LocalDateTime dataCadastroInicio,
			@Param("dataCadastroFim") java.time.LocalDateTime dataCadastroFim,
			@Param("categoriaItemId") Long categoriaItemId, @Param("localizacaoId") Long localizacaoId,
			@Param("statusItemId") Long statusItemId, @Param("nomeItem") String nomeItem,
			@Param("codigoItem") String codigoItem, @Param("numeroSerie") String numeroSerie,
			@Param("notaFiscal") String notaFiscal, @Param("termoPesquisa") String termoPesquisa, Pageable pageable);

	Optional<Item> findByCodigoItem(String codigoItem);

	boolean existsByLocalizacaoId(Long localizacaoId);

	boolean existsByCategoriaItemId(Long categoriaId);

	@Query("""
			SELECT new br.com.fatec.mogi.inventory_service.coreService.web.response.ItemPorCategoriaResponseDTO(
				c.nome,
				COUNT(i.id)
			)
			FROM Item i
			JOIN i.categoriaItem c
			WHERE i.localizacao.id = :localizacaoId
			GROUP BY c.nome
			ORDER BY COUNT(i.id) DESC
			""")
	List<ItemPorCategoriaResponseDTO> countByCategoriaAndLocalizacaoId(@Param("localizacaoId") Long localizacaoId);

	boolean existsByStatusItemId(Long statusItemID);

}

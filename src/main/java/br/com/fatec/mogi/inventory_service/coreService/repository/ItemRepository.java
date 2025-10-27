package br.com.fatec.mogi.inventory_service.coreService.repository;

import br.com.fatec.mogi.inventory_service.coreService.domain.model.Item;
import br.com.fatec.mogi.inventory_service.coreService.web.response.ItemPorCategoriaResponseDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.ItemResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

	boolean existsByCodigoItem(String codigo);

	@Query("""
			SELECT new br.com.fatec.mogi.inventory_service.coreService.web.response.ItemResponseDTO(
				i.id,
				i.nomeItem,
				i.numeroSerie,
				i.statusItem,
				i.categoriaItem,
				i.tipoEntrada,
				i.codigoItem,
				i.dataCadastro,
				i.localizacao,
				i.notaFiscal
			)
			FROM Item i
			LEFT JOIN i.localizacao l
			LEFT JOIN i.categoriaItem c
			LEFT JOIN i.statusItem s
			WHERE (:dataCadastroInicio IS NULL OR i.dataCadastro >= :dataCadastroInicio)
			  AND (:dataCadastroFim IS NULL OR i.dataCadastro <= :dataCadastroFim)
			  AND (:categoriaItemId IS NULL OR i.categoriaItem.id = :categoriaItemId)
			  AND (:localizacaoId IS NULL OR i.localizacao.id = :localizacaoId)
			  AND (:statusItemId IS NULL OR i.statusItem.id = :statusItemId)
			  AND (:nomeItem IS NULL OR upper(i.nomeItem) LIKE concat('%', upper(:nomeItem), '%'))
			  AND (:codigoItem IS NULL OR upper(i.codigoItem) LIKE concat('%', upper(:codigoItem), '%'))
			  AND (:numeroSerie IS NULL OR upper(i.numeroSerie) LIKE concat('%', upper(:numeroSerie), '%'))
			  AND (:notaFiscal IS NULL OR upper(i.notaFiscal) LIKE concat('%', upper(:notaFiscal), '%'))
			  AND (
			    :termoPesquisa IS NULL OR
			    upper(concat(
			      COALESCE(i.codigoItem, ''), ' ',
			      COALESCE(i.numeroSerie, ''), ' ',
			      COALESCE(FUNCTION('TO_CHAR', i.dataCadastro, 'DD/MM/YYYY'), ''), ' ',
			      COALESCE(l.nomeSala, ''), ' ',
			      COALESCE(c.nome, ''), ' ',
			      COALESCE(s.nome, '')
			    )) LIKE concat('%', upper(trim(:termoPesquisa)), '%')
			  )
			""")
	Page<ItemResponseDTO> filtrar(@Param("dataCadastroInicio") java.time.LocalDateTime dataCadastroInicio,
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

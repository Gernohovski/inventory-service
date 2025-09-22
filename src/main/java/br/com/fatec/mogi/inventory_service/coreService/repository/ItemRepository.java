package br.com.fatec.mogi.inventory_service.coreService.repository;

import br.com.fatec.mogi.inventory_service.coreService.domain.model.Item;
import br.com.fatec.mogi.inventory_service.coreService.web.response.ItemResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
			WHERE i.dataCadastro >= COALESCE(:dataCadastroInicio, i.dataCadastro)
			  and i.dataCadastro <= COALESCE(:dataCadastroFim, i.dataCadastro)
			  and i.categoriaItem.id = COALESCE(:categoriaItemId, i.categoriaItem.id)
			  and i.localizacao.id   = COALESCE(:localizacaoId, i.localizacao.id)
			  and i.statusItem.id    = COALESCE(:statusItemId, i.statusItem.id)
			  and i.tipoEntrada.id   = COALESCE(:tipoEntradaId, i.tipoEntrada.id)
			  and upper(i.nomeItem)   like COALESCE(concat('%', upper(:nomeItem), '%'), upper(i.nomeItem))
			  and upper(i.codigoItem) like COALESCE(concat('%', upper(:codigoItem), '%'), upper(i.codigoItem))
			  and upper(i.numeroSerie) like COALESCE(concat('%', upper(:numeroSerie), '%'), upper(i.numeroSerie))
			  and upper(i.notaFiscal) like COALESCE(concat('%', upper(:notaFiscal), '%'), upper(i.notaFiscal))
			""")
	Page<ItemResponseDTO> filtrar(@Param("dataCadastroInicio") java.time.LocalDateTime dataCadastroInicio,
			@Param("dataCadastroFim") java.time.LocalDateTime dataCadastroFim,
			@Param("categoriaItemId") Long categoriaItemId, @Param("localizacaoId") Long localizacaoId,
			@Param("statusItemId") Long statusItemId, @Param("tipoEntradaId") Long tipoEntradaId,
			@Param("nomeItem") String nomeItem, @Param("codigoItem") String codigoItem,
			@Param("numeroSerie") String numeroSerie, @Param("notaFiscal") String notaFiscal, Pageable pageable);

	Optional<Item> findByCodigoItem(String codigoItem);

}

package br.com.fatec.mogi.inventory_service.coreService.repository;

import br.com.fatec.mogi.inventory_service.coreService.domain.model.Item;
import br.com.fatec.mogi.inventory_service.coreService.web.response.ItemResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, Long> {

	boolean existsByCodigoItem(String codigo);

	@Query(value = """
			    select new br.com.fatec.mogi.inventory_service.coreService.web.response.ItemResponseDTO(
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
			    from Item i
			    where (:dataCadastroInicio is null or i.dataCadastro >= :dataCadastroInicio)
			      and (:dataCadastroFim    is null or i.dataCadastro <= :dataCadastroFim)
			      and (:categoriaItemId   is null or i.categoriaItem.id = :categoriaItemId)
			      and (:localizacaoId     is null or i.localizacao.id   = :localizacaoId)
			      and (:statusItemId      is null or i.statusItem.id    = :statusItemId)
			      and (:tipoEntradaId     is null or i.tipoEntrada.id   = :tipoEntradaId)
			      and (:nomeItem          is null or upper(i.nomeItem)   like concat('%', upper(:nomeItem), '%'))
			      and (:codigoItem        is null or upper(i.codigoItem) like concat('%', upper(:codigoItem), '%'))
			      and (:numeroSerie       is null or upper(i.numeroSerie) like concat('%', upper(:numeroSerie), '%'))
			      and (:notaFiscal        is null or upper(i.notaFiscal) like concat('%', upper(:notaFiscal), '%'))
			""")
	Page<ItemResponseDTO> filtrar(@Param("dataCadastroInicio") java.time.LocalDateTime dataCadastroInicio,
			@Param("dataCadastroFim") java.time.LocalDateTime dataCadastroFim,
			@Param("categoriaItemId") Long categoriaItemId, @Param("localizacaoId") Long localizacaoId,
			@Param("statusItemId") Long statusItemId, @Param("tipoEntradaId") Long tipoEntradaId,
			@Param("nomeItem") String nomeItem, @Param("codigoItem") String codigoItem,
			@Param("numeroSerie") String numeroSerie, @Param("notaFiscal") String notaFiscal, Pageable pageable);

}

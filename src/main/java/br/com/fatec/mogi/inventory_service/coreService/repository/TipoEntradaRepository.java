package br.com.fatec.mogi.inventory_service.coreService.repository;

import br.com.fatec.mogi.inventory_service.coreService.domain.model.TipoEntrada;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoEntradaRepository extends JpaRepository<TipoEntrada, Long> {

	Optional<TipoEntrada> findByNome(String nome);

}

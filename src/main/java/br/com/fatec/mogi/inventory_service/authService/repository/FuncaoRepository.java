package br.com.fatec.mogi.inventory_service.authService.repository;

import br.com.fatec.mogi.inventory_service.authService.domain.model.Funcao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FuncaoRepository extends JpaRepository<Funcao, Long> {

	Optional<Funcao> findByNome(String nome);

}

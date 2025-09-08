package br.com.fatec.mogi.inventory_service.authService.repository;

import br.com.fatec.mogi.inventory_service.authService.domain.model.Funcionalidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FuncionalidadeRepository extends JpaRepository<Funcionalidade, Long> {

	Optional<Funcionalidade> findByFuncionalidade(String funcionalidade);

}

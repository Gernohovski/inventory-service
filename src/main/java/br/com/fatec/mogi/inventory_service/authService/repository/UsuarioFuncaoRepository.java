package br.com.fatec.mogi.inventory_service.authService.repository;

import br.com.fatec.mogi.inventory_service.authService.domain.model.UsuarioFuncao;
import br.com.fatec.mogi.inventory_service.authService.domain.model.UsuarioFuncaoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioFuncaoRepository extends JpaRepository<UsuarioFuncao, UsuarioFuncaoId> {

	List<UsuarioFuncao> findByUsuarioId(Long usuarioId);

}

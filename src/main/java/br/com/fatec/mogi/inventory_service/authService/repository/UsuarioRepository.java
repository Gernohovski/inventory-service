package br.com.fatec.mogi.inventory_service.authService.repository;


import br.com.fatec.mogi.inventory_service.authService.domain.model.Usuario;
import br.com.fatec.mogi.inventory_service.authService.domain.model.valueObjects.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Optional<Usuario> findByEmail(Email email);

	@Query("""
				SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END
				FROM UsuarioFuncao uf
				JOIN uf.funcao f
				JOIN f.funcionalidades func
				WHERE uf.usuario.id = :usuarioId
				  AND func.funcionalidade = :funcionalidade
			""")
	boolean possuiFuncionalidade(@Param("usuarioId") Long usuarioId, @Param("funcionalidade") String funcionalidade);

}

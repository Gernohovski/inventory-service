package br.com.fatec.mogi.inventory_service.authService.repository;

import br.com.fatec.mogi.inventory_service.authService.domain.model.Usuario;
import br.com.fatec.mogi.inventory_service.authService.domain.model.valueObjects.Email;
import br.com.fatec.mogi.inventory_service.authService.web.dto.response.UsuarioResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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

	@Query("""
			SELECT DISTINCT NEW br.com.fatec.mogi.inventory_service.authService.web.dto.response.UsuarioResponseDTO(
				u.id,
				u.nome,
				u.email.email,
				u.ativo,
				u.podeRealizarAuditoria
			)
			FROM Usuario u
			LEFT JOIN UsuarioFuncao uf ON uf.usuario = u
			LEFT JOIN uf.funcao f
			WHERE u.ativo = TRUE
			  AND (:#{#dto.termoPesquisa} IS NULL OR
			      UPPER(CONCAT(
			          COALESCE(u.nome, ''), ' ',
			          COALESCE(u.email.email, ''), ' ',
			          CASE WHEN u.ativo = TRUE THEN 'ativo' ELSE 'inativo' END, ' ',
			          COALESCE(f.nome, '')
			      )) LIKE CONCAT('%', UPPER(TRIM(COALESCE(:#{#dto.termoPesquisa}, ''))), '%'))
			""")
	Page<UsuarioResponseDTO> findAllUsuarios(Pageable pageable,
			@Param("dto") br.com.fatec.mogi.inventory_service.authService.web.dto.request.ConsultarUsuarioRequestDTO dto);

	@Query("""
			SELECT NEW br.com.fatec.mogi.inventory_service.authService.web.dto.response.UsuarioResponseDTO(
				 u.id,
			     u.nome,
				 u.email.email,
				 u.ativo,
				 u.podeRealizarAuditoria
			)
			FROM Usuario u
			JOIN UsuarioFuncao uf ON uf.usuario.id = u.id
			WHERE uf.funcao.nome = 'ADMIN'
			""")
	List<UsuarioResponseDTO> findUsuariosAdministradores();

}

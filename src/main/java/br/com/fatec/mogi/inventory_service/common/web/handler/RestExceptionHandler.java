package br.com.fatec.mogi.inventory_service.common.web.handler;

import br.com.fatec.mogi.inventory_service.authService.domain.exception.UsuarioNaoAutenticadoException;
import br.com.fatec.mogi.inventory_service.authService.domain.exception.UsuarioNaoAutorizadoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> tratarIllegalArgumentException(IllegalArgumentException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(UsuarioNaoAutenticadoException.class)
	public ResponseEntity<String> tratarUsuarioNaoAutenticado(UsuarioNaoAutenticadoException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
	}

	@ExceptionHandler(UsuarioNaoAutorizadoException.class)
	public ResponseEntity<String> tratarUsuarioNaoAutorizado(UsuarioNaoAutorizadoException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> tratarRuntimeException(RuntimeException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(SQLException.class)
	public ResponseEntity<String> tratarSqlException(SQLException ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro, tente novamente mais tarde.");
	}

}
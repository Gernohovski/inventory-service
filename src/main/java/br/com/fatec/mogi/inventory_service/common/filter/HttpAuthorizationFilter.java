package br.com.fatec.mogi.inventory_service.common.filter;

import br.com.fatec.mogi.inventory_service.authService.service.AutorizacaoService;
import br.com.fatec.mogi.inventory_service.authService.web.dto.request.AutorizarUsuarioRequestDTO;
import br.com.fatec.mogi.inventory_service.common.domain.enums.FuncionalidadesEnum;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@Order(1)
public class HttpAuthorizationFilter implements Filter {

	@Autowired
	private AutorizacaoService autorizacaoService;

	private static final List<String> EXCLUDED_PATHS = Arrays.asList("/auth-service/v1/autenticacao/login",
			"/auth-service/v1/usuarios/solicitar-redefinicao-senha", "/auth-service/v1/usuarios/alterar-senha");

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String requestPath = httpRequest.getRequestURI();
		String method = httpRequest.getMethod();

		if ("OPTIONS".equals(method) || isExcludedPath(requestPath)) {
			chain.doFilter(request, response);
			return;
		}

		try {
			String accessToken = httpRequest.getHeader("X-ACCESS-TOKEN");
			if (accessToken == null) {
				sendUnauthorizedResponse(httpResponse, "Token de autorização inválido.");
				return;
			}

			FuncionalidadesEnum funcionalidade = FuncionalidadesEnum.getByEndpoint(requestPath);
			if (funcionalidade == null) {
				sendUnauthorizedResponse(httpResponse, "Rota não mapeada.");
				return;
			}

			var autorizacaoRequestDto = AutorizarUsuarioRequestDTO.builder()
				.funcionalidade(funcionalidade.toString())
				.build();

			autorizacaoService.autorizar(autorizacaoRequestDto, accessToken);
			chain.doFilter(request, response);
		}
		catch (Exception e) {
			sendUnauthorizedResponse(httpResponse, "Erro de autenticação.");
		}

	}

	private boolean isExcludedPath(String requestPath) {
		return EXCLUDED_PATHS.contains(requestPath);
	}

	private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		response.getWriter()
			.write(String.format("{\"error\": \"Unauthorized\", \"message\": \"%s\", \"status\": 401}", message));
	}

}

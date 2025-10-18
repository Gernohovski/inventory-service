package br.com.fatec.mogi.inventory_service.common.filter;

import br.com.fatec.mogi.inventory_service.authService.service.AutorizacaoService;
import br.com.fatec.mogi.inventory_service.authService.web.dto.request.AutorizarUsuarioRequestDTO;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@Component
@Order(1)
public class HttpAuthorizationFilter implements Filter {

	private static final Logger LOG = Logger.getLogger(HttpAuthorizationFilter.class.getName());

	@Autowired
	private AutorizacaoService autorizacaoService;

	private static final List<String> EXCLUDED_PATHS = Arrays.asList("/auth-service/v1/autenticacao/login",
			"/auth-service/v1/usuarios/solicitar-redefinicao-senha", "/auth-service/v1/usuarios/alterar-senha",
			"/auth-service/v1/autenticacao/refresh");

	private static final Pattern PATH_VARIABLE_PATTERN = Pattern
		.compile("/\\d+|/[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}");

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
		String normalizedPath = normalizePath(requestPath);
		String method = httpRequest.getMethod();

		LOG.info("Requisição recebida: " + httpRequest);
		LOG.info("Request PATH original: " + requestPath);
		LOG.info("Request PATH normalizado: " + normalizedPath);
		LOG.info("Request Method " + method);

		httpResponse.setHeader("Access-Control-Allow-Origin", "*");
		httpResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
		httpResponse.setHeader("Access-Control-Allow-Headers", "Authorization,Content-Type,X-ACCESS-TOKEN");
		httpResponse.setHeader("Access-Control-Allow-Credentials", "true");

		if ("OPTIONS".equalsIgnoreCase(method)) {
			httpResponse.setStatus(HttpServletResponse.SC_OK);
			return;
		}

		if (isExcludedPath(normalizedPath)) {
			chain.doFilter(request, response);
			return;
		}

		try {
			String accessToken = httpRequest.getHeader("X-ACCESS-TOKEN");
			if (accessToken == null) {
				sendUnauthorizedResponse(httpResponse, "Token de autorização inválido.");
				return;
			}

			var autorizacaoRequestDto = AutorizarUsuarioRequestDTO.builder()
				.endpoint(normalizedPath)
				.httpMethod(method)
				.build();

			autorizacaoService.autorizar(autorizacaoRequestDto, accessToken);
			chain.doFilter(request, response);
		}
		catch (Exception e) {
			LOG.info(e.getMessage());
			sendUnauthorizedResponse(httpResponse, "Erro de autenticação.");
		}
	}

	private boolean isExcludedPath(String requestPath) {
		return EXCLUDED_PATHS.contains(requestPath);
	}

	private String normalizePath(String requestPath) {
		if (requestPath == null || requestPath.isEmpty()) {
			return requestPath;
		}
		String normalizedPath = PATH_VARIABLE_PATTERN.matcher(requestPath).replaceAll("");
		normalizedPath = normalizedPath.replaceAll("//+", "/");
		if (normalizedPath.length() > 1 && normalizedPath.endsWith("/")) {
			normalizedPath = normalizedPath.substring(0, normalizedPath.length() - 1);
		}
		return normalizedPath;
	}

	private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		response.getWriter()
			.write(String.format("{\"error\": \"Unauthorized\", \"message\": \"%s\", \"status\": 401}", message));
	}

}
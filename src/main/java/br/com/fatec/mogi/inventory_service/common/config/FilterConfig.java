package br.com.fatec.mogi.inventory_service.common.config;

import br.com.fatec.mogi.inventory_service.common.filter.HttpAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

	@Autowired
	private HttpAuthorizationFilter httpAuthorizationFilter;

	@Bean
	public FilterRegistrationBean<HttpAuthorizationFilter> authorizationFilter() {
		FilterRegistrationBean<HttpAuthorizationFilter> registrationBean = new FilterRegistrationBean<>();

		registrationBean.setFilter(httpAuthorizationFilter);
		registrationBean.addUrlPatterns("*");
		registrationBean.setOrder(1);
		registrationBean.setName("HttpAuthorizationFilter");

		return registrationBean;
	}

}

package com.lk.cloud.role;

import java.util.Arrays;

import com.lk.cloud.role.config.auth.IAuthenticationFacade;
import com.lk.cloud.role.config.auth.RestTemplateInterceptor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication
public class RoleManagementApplication {

	private final IAuthenticationFacade authenticationFacade;

	public RoleManagementApplication(IAuthenticationFacade authenticationFacade) {
		this.authenticationFacade = authenticationFacade;
	}
	public static void main(String[] args) {
		SpringApplication.run(RoleManagementApplication.class, args);
	}

	@Bean("securedInternalRestTemplate")
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getInterceptors()
			.add(new RestTemplateInterceptor(authenticationFacade));
		return restTemplate;
	}

	@Bean
	public CorsFilter corsFilter() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		final CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.setAllowedOriginPatterns(Arrays.asList("http://*", "chrome-extension**", "**"));
		config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization", "Access-Control-Request-Method", "Access-Control-Request-Headers"));
		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

}

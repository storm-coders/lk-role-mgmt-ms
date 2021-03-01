package com.lk.cloud.role;

import com.lk.cloud.role.config.auth.IAuthenticationFacade;
import com.lk.cloud.role.config.auth.RestTemplateInterceptor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

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

}

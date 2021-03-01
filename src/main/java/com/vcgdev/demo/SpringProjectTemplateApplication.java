package com.vcgdev.demo;

import com.vcgdev.demo.config.auth.IAuthenticationFacade;
import com.vcgdev.demo.config.auth.RestTemplateInterceptor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SpringProjectTemplateApplication {

	private final IAuthenticationFacade authenticationFacade;

	public SpringProjectTemplateApplication(IAuthenticationFacade authenticationFacade) {
		this.authenticationFacade = authenticationFacade;
	}
	public static void main(String[] args) {
		SpringApplication.run(SpringProjectTemplateApplication.class, args);
	}

	@Bean("securedInternalRestTemplate")
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getInterceptors()
			.add(new RestTemplateInterceptor(authenticationFacade));
		return restTemplate;
	}

}

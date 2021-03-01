package com.vcgdev.demo.config.auth;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {

    private IAuthenticationFacade authFacade;

    public RestTemplateInterceptor(IAuthenticationFacade authFacade) {
        this.authFacade = authFacade;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        if (!request.getHeaders().containsKey("Authorization")) {
            Object details = authFacade.getAuthentication().getDetails();
            if (details instanceof OAuth2AuthenticationDetails) {
                OAuth2AuthenticationDetails oauthDetails = (OAuth2AuthenticationDetails) details;
                request.getHeaders().add("Authorization", oauthDetails.getTokenType() + " " + oauthDetails.getTokenValue());        
            }
        }
        return execution.execute(request, body);
    }
}
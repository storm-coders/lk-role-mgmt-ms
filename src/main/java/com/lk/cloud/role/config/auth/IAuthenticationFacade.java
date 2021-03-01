package com.lk.cloud.role.config.auth;

import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {
    String getAuthenticatedUser();
    Authentication getAuthentication();
}

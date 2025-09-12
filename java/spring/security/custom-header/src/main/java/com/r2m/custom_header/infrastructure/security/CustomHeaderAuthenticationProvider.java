package com.r2m.custom_header.infrastructure.security;

import com.r2m.custom_header.model.exceptions.InvalidAuthenticationException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomHeaderAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userId = (String) authentication.getPrincipal();

        return validateUserId(userId);
    }

    private Authentication validateUserId(String userId) {
        if(!"authUser".equals(userId)){
            throw new InvalidAuthenticationException("Invalid user authentication");
        }

        return new PreAuthenticatedAuthenticationToken(userId, "ROLE_ADMIN", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

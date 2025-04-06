package lanz.global.financeservice.facade.impl;

import lanz.global.financeservice.facade.AuthenticationFacade;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public UUID getCompanyId() {
        Authentication authentication = getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String companyId = (String) jwt.getClaims().get("companyId");

        return UUID.fromString(companyId);
    }
}

package lanz.global.financeservice.facade;

import org.springframework.security.core.Authentication;

import java.util.UUID;

public interface AuthenticationFacade {
    Authentication getAuthentication();
    UUID getCompanyId();
}

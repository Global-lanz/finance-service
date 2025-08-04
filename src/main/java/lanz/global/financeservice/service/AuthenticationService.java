package lanz.global.financeservice.service;

import lanz.global.financeservice.config.ServiceConfig;
import lanz.global.financeservice.external.api.m2m.AuthenticationClient;
import lanz.global.financeservice.external.api.m2m.request.ServiceAuthenticationRequest;
import lanz.global.financeservice.external.api.m2m.response.ServiceAuthenticationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationClient client;
    private final ServiceConfig config;

    private ServiceAuthenticationResponse cachedToken;

    private final Object tokenLock = new Object();

    public String getServiceToken() {
        synchronized (tokenLock) {
            if (cachedToken != null) {
                return cachedToken.accessToken();
            }

            // Obter novo token
            cachedToken = requestNewToken();
            return cachedToken != null ? cachedToken.accessToken() : null;
        }
    }

    private ServiceAuthenticationResponse requestNewToken() {
        try {
            ServiceAuthenticationRequest request = new ServiceAuthenticationRequest(
                    config.getServiceName(),
                    config.getSecurity().getApiSecret()
            );

            return client.serviceAuthentication(request);

        } catch (Exception e) {
            log.error("Failed to obtain service token", e);
            throw new RuntimeException("Failed to obtain service token", e);
        }
    }

}

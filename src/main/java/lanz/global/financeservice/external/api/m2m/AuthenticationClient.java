package lanz.global.financeservice.external.api.m2m;

import lanz.global.financeservice.external.api.m2m.request.ServiceAuthenticationRequest;
import lanz.global.financeservice.external.api.m2m.response.ServiceAuthenticationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "AUTHENTICATION-SERVICE")
public interface AuthenticationClient {


    @PostMapping("authentication/m2m/login")
    ServiceAuthenticationResponse serviceAuthentication(@RequestBody ServiceAuthenticationRequest request);
}

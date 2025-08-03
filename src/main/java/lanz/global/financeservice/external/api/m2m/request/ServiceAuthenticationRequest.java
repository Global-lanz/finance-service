package lanz.global.financeservice.external.api.m2m.request;

import jakarta.validation.constraints.NotBlank;

public record ServiceAuthenticationRequest(@NotBlank String serviceName, @NotBlank String serviceSecret) {

}

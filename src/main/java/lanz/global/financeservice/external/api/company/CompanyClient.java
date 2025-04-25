package lanz.global.financeservice.external.api.company;

import lanz.global.financeservice.external.api.company.response.CompanyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "COMPANY-SERVICE")
public interface CompanyClient {

    @GetMapping("/company/{companyId}")
    ResponseEntity<CompanyResponse> findCompanyById(@PathVariable UUID companyId);

}

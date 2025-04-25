package lanz.global.financeservice.external.api.customer;

import lanz.global.financeservice.external.api.customer.response.CustomerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "CUSTOMER-SERVICE")
public interface CustomerClient {

    @GetMapping("/customer/{customerId}")
    CustomerResponse findCustomerById(@PathVariable UUID customerId);

}

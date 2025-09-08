package lanz.global.financeservice.util.fetcher;

import lanz.global.financeservice.external.api.customer.response.CustomerResponse;
import lanz.global.financeservice.service.CustomerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;
import java.util.function.Function;

@Configuration
public class CustomerFetcher {

    @Bean
    public Function<UUID, CustomerResponse> customerFetcher(CustomerService customerService) {
        return customerService::findCustomerById;
    }
}

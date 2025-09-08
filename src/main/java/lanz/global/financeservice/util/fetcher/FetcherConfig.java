package lanz.global.financeservice.util.fetcher;

import lanz.global.financeservice.external.api.customer.response.CustomerResponse;
import lanz.global.financeservice.service.CustomerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.UUID;
import java.util.function.Function;

@Configuration
public class FetcherConfig {

    @Bean
    public Function<UUID, CustomerResponse> customerFetcher(@Lazy CustomerService customerService) {
        return customerService::findCustomerById;
    }
}

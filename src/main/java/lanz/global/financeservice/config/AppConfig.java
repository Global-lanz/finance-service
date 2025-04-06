package lanz.global.financeservice.config;

import lanz.global.financeservice.security.HeaderInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AppConfig {

    private final HeaderInterceptor headerInterceptor;

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(headerInterceptor);
        return restTemplate;
    }
}

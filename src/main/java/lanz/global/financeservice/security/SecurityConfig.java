package lanz.global.financeservice.security;

import lanz.global.financeservice.config.ServiceConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableMethodSecurity(jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String ROLE_SERVICE = "ROLE_SERVICE";
    private final ServiceConfig config;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET, "/v3/api-docs").permitAll()
                        .requestMatchers(HttpMethod.GET, "/finance/currency/**").permitAll()
                        .requestMatchers("/finance/**").hasAnyRole(ROLE_SERVICE)
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKey key = new SecretKeySpec(config.getSecurity().getApiSecret().getBytes(), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        var converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new CustomRoleConverter());
        return converter;
    }

}

package lanz.global.financeservice.security;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class HeaderInterceptor implements RequestInterceptor {

    private final HttpServletRequest request;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Stream.ofNullable(requestTemplate.headers())
                .flatMap(headers -> headers.entrySet().stream())
                .forEach(entry -> requestTemplate.header(entry.getKey(), entry.getValue()));
    }
}

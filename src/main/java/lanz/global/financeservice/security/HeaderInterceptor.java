package lanz.global.financeservice.security;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lanz.global.financeservice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@RequiredArgsConstructor
public class HeaderInterceptor implements RequestInterceptor {

    private final AuthenticationService authenticationService;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            if (request.getHeaderNames() != null) {
                request.getHeaderNames().asIterator().forEachRemaining(headerName -> {
                    String headerValue = request.getHeader(headerName);
                    if (headerValue != null) {
                        requestTemplate.header(headerName, headerValue);
                    }
                });
            }
        } else {
            String serviceToken = String.format("Bearer %s", authenticationService.getServiceToken());
            requestTemplate.header("Authorization", serviceToken);
        }
    }
}

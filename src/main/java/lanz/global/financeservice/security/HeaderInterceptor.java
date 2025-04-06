package lanz.global.financeservice.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HeaderInterceptor implements ClientHttpRequestInterceptor {

    private final HttpServletRequest request;

    @Override
    public ClientHttpResponse intercept(
            HttpRequest httpRequest,
            byte[] body,
            ClientHttpRequestExecution execution) throws IOException {


        String authorization = request.getHeader("Authorization");
        String accept = request.getHeader("Accept");
        String language = request.getHeader("accept-language");


        HttpHeaders headers = httpRequest.getHeaders();
        headers.add("Authorization", authorization);
        headers.add("Accept", accept);
        headers.add("accept-language", language);

        return execution.execute(httpRequest, body);
    }

}

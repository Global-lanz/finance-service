package lanz.global.financeservice.service;

import jakarta.ws.rs.InternalServerErrorException;
import lanz.global.financeservice.exception.BadRequestException;
import lanz.global.financeservice.external.api.response.CustomerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerService {

    private final RestTemplate restTemplate;

    private static final String URL = "http://customer-service:80/customer";

    public UUID findCustomerById(UUID customerId) throws BadRequestException {
        ResponseEntity<CustomerResponse> response = restTemplate.getForEntity(URL + "/" + customerId.toString(), CustomerResponse.class);

        return switch (response.getStatusCode()) {
            case HttpStatus.OK -> response.getBody().customerId();
            case HttpStatus.BAD_REQUEST -> throw new BadRequestException("exception.create-bad-request.title", "exception.create-bad-request.message", "Customer");
            case null, default -> throw new InternalServerErrorException();
        };
    }

}

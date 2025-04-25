package lanz.global.financeservice.service;

import jakarta.ws.rs.InternalServerErrorException;
import lanz.global.financeservice.exception.BadRequestException;
import lanz.global.financeservice.external.api.customer.CustomerClient;
import lanz.global.financeservice.external.api.customer.response.CustomerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerService {

    private final CustomerClient customerClient;

    public UUID findCustomerById(UUID customerId) throws BadRequestException {
        ResponseEntity<CustomerResponse> response = customerClient.findCustomerById(customerId);

        return switch (response.getStatusCode()) {
            case HttpStatus.OK -> response.getBody().customerId();
            case HttpStatus.BAD_REQUEST ->
                    throw new BadRequestException("exception.create-bad-request.title", "exception.create-bad-request.message", "Customer");
            case null, default -> throw new InternalServerErrorException();
        };
    }

}

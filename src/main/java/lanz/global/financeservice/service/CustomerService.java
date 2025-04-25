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
        return customerClient.findCustomerById(customerId).customerId();
    }

}

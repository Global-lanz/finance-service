package lanz.global.financeservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class InvoicePDFNotFound extends ServiceException {

    @Serial
    private static final long serialVersionUID = -853167422656547594L;

    public InvoicePDFNotFound() {
        super("exception.invoice-pdf-not-found.title", "exception.invoice-pdf-not-found.message");
    }
}

package lanz.global.financeservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class BadRequestException extends ServiceException {

    @Serial
    private static final long serialVersionUID = -6140622279424182367L;

    public BadRequestException(String argument) {
        super("exception.bad-request.title", "exception.bad-request.message", argument);
    }

    public BadRequestException(String title, String message, String... args) {
        super(title, message, args);
    }
}

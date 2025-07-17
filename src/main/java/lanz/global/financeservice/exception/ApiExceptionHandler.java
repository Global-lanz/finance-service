package lanz.global.financeservice.exception;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lanz.global.financeservice.exception.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;

@Log4j2
@ControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;
    private final FeignErrorDecoder feignErrorDecoder;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleError(HttpServletRequest req, Exception ex) {
        String title = messageSource.getMessage("exception.internal-server-error.title", null, getLocale(req));
        String message = messageSource.getMessage("exception.internal-server-error.message", null, getLocale(req));
        log.error(message, ex);
        return ResponseEntity.internalServerError().body(createErrorDTO(title, message));
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> handleServiceExceptionError(HttpServletRequest req, ServiceException ex) {
        String title = messageSource.getMessage(ex.getTitle(), null, getLocale(req));
        String message = messageSource.getMessage(ex.getMessage(), ex.getArguments(), getLocale(req));
        HttpStatus status = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class).code();
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(status.value()).body(createErrorDTO(title, message));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(HttpServletRequest req, BadCredentialsException ex) {
        String title = messageSource.getMessage("exception.bad-credentials-exception.title", null, getLocale(req));
        String message = messageSource.getMessage("exception.bad-credentials-exception.message", null, getLocale(req));
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(createErrorDTO(title, message));
    }

    @ExceptionHandler(AccountExpiredException.class)
    public ResponseEntity<ErrorResponse> handleAccountExpiredException(HttpServletRequest req, AccountExpiredException ex) {
        String title = messageSource.getMessage("exception.account-expired.title", null, getLocale(req));
        String message = messageSource.getMessage("exception.account-expired.message", null, getLocale(req));
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(createErrorDTO(title, message));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationDeniedException(HttpServletRequest req, AuthorizationDeniedException ex) {
        String title = messageSource.getMessage("exception.authorization-denied-exception.title", null, getLocale(req));
        String message = messageSource.getMessage("exception.authorization-denied-exception.message", null, getLocale(req));
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(createErrorDTO(title, message));
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeignException(HttpServletRequest req, FeignException ex) {
        return ResponseEntity.status(ex.status()).body(feignErrorDecoder.decode(ex));
    }


    private ErrorResponse createErrorDTO(String title, String message) {
        return new ErrorResponse(title, message);
    }

    private Locale getLocale(HttpServletRequest req) {
        return req.getLocale();
    }
}

package lanz.global.financeservice.exception;

import lombok.Getter;

import java.io.Serial;

@Getter
public class ServiceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6961242570378717595L;

    private final String title;
    private final String message;
    private final String[] arguments;

    public ServiceException(String title, String message, String... arguments) {
        super(message);
        this.title = title;
        this.message = message;
        this.arguments = arguments;
    }

  public ServiceException(String title, String message) {
    super(message);
    this.title = title;
    this.message = message;
    this.arguments = null;
  }

}

package by.knowledgeportal.exception;

public class EnumNotFoundException extends RuntimeException{

    public EnumNotFoundException() {
    }

    public EnumNotFoundException(String message) {
        super(message);
    }

    public EnumNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EnumNotFoundException(Throwable cause) {
        super(cause);
    }

    public EnumNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

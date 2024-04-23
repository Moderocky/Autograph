package org.valross.autograph.error;

public class AutographException extends RuntimeException {

    public AutographException() {
        super();
    }

    public AutographException(String message) {
        super(message);
    }

    public AutographException(String message, Throwable cause) {
        super(message, cause);
    }

    public AutographException(Throwable cause) {
        super(cause);
    }

    protected AutographException(String message, Throwable cause, boolean enableSuppression,
                                 boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

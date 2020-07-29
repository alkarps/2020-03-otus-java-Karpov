package my.alkarps.atm.model.exception;

/**
 * @author alkarps
 * create date 27.07.2020 9:07
 */
public abstract class AtmException extends RuntimeException {
    public AtmException(String message) {
        super(message);
    }

    public AtmException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

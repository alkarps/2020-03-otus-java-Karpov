package my.alkarps.engine.exception.validate;

/**
 * @author alkarps
 * create date 13.07.2020 11:08
 */
public abstract class NotValidClassException extends RuntimeException {
    public NotValidClassException(String message) {
        super("Класс не валиден: " + message);
    }
}

package my.alkarps.engine;

/**
 * @author alkarps
 * create date 13.07.2020 11:08
 */
public class NotValidClassException extends RuntimeException {
    public NotValidClassException(Throwable throwable) {
        super("Класс не валиден: " + throwable.getMessage(), throwable);
    }

    public NotValidClassException(String message) {
        super("Класс не валиден: " + message);
    }
}

package my.alkarps.engine.exception;

/**
 * @author alkarps
 * create date 13.07.2020 11:08
 */
public abstract class TestClassException extends RuntimeException {
    public TestClassException(String message) {
        super("Класс не валиден: " + message);
    }

    public TestClassException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

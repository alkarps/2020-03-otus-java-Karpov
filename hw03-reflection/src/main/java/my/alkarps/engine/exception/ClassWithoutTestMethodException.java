package my.alkarps.engine.exception;

/**
 * @author alkarps
 * create date 23.07.2020 9:15
 */
public class ClassWithoutTestMethodException extends NotValidClassException {
    public ClassWithoutTestMethodException() {
        super("Отсутствуют методы для тестирования.");
    }
}

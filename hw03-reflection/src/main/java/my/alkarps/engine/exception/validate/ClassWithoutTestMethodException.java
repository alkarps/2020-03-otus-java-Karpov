package my.alkarps.engine.exception.validate;

import my.alkarps.engine.exception.TestClassException;

/**
 * @author alkarps
 * create date 23.07.2020 9:15
 */
public class ClassWithoutTestMethodException extends TestClassException {
    public ClassWithoutTestMethodException() {
        super("Отсутствуют методы для тестирования.");
    }
}

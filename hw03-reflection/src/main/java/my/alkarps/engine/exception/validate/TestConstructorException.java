package my.alkarps.engine.exception.validate;

import my.alkarps.engine.exception.TestClassException;

/**
 * @author alkarps
 * create date 23.07.2020 9:12
 */
public class TestConstructorException extends TestClassException {
    public TestConstructorException() {
        super("Конструктор класса должен быть public и быть без аргументов");
    }
}

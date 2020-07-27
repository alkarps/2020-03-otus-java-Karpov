package my.alkarps.engine.exception.validate;

import my.alkarps.engine.exception.TestClassException;

/**
 * @author alkarps
 * create date 23.07.2020 9:09
 */
public class ClassNotFoundException extends TestClassException {
    public ClassNotFoundException() {
        super("Класс не указан.");
    }
}

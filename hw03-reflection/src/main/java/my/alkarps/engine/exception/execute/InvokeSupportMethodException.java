package my.alkarps.engine.exception.execute;

import my.alkarps.engine.exception.TestClassException;

/**
 * @author alkarps
 * create date 23.07.2020 15:42
 */
public class InvokeSupportMethodException extends TestClassException {
    public InvokeSupportMethodException(Throwable throwable) {
        super("Ошибка при выполнении статических вспомогательных методов", throwable);
    }
}

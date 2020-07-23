package my.alkarps.engine.exception.execute;

/**
 * @author alkarps
 * create date 23.07.2020 15:42
 */
public class InvokeSupportMethodException extends RuntimeException {
    public InvokeSupportMethodException(Throwable throwable) {
        super("Ошибка при выполнении статических вспомогательных методов", throwable);
    }
}

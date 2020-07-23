package my.alkarps.engine.exception.execute;

/**
 * @author alkarps
 * create date 23.07.2020 15:38
 */
public class CreateTestInstanceClassException extends RuntimeException {
    public CreateTestInstanceClassException(Throwable throwable) {
        super("Ошибка инициализации тестового класса", throwable);
    }
}

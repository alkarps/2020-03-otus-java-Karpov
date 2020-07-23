package my.alkarps.engine.exception.validate;

/**
 * @author alkarps
 * create date 23.07.2020 9:12
 */
public class NotValidConstructorException extends NotValidClassException {
    public NotValidConstructorException() {
        super("Конструктор класса должен быть public и быть без аргументов");
    }
}

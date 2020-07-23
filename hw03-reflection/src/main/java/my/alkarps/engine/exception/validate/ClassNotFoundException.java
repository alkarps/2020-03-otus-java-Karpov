package my.alkarps.engine.exception.validate;

/**
 * @author alkarps
 * create date 23.07.2020 9:09
 */
public class ClassNotFoundException extends NotValidClassException {
    public ClassNotFoundException() {
        super("Класс не указан.");
    }
}

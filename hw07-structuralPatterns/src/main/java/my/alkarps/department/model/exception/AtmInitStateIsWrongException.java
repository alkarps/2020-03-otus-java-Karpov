package my.alkarps.department.model.exception;

/**
 * @author alkarps
 * create date 30.07.2020 12:56
 */
public class AtmInitStateIsWrongException extends DepartmentException {
    public AtmInitStateIsWrongException() {
        super("Начальное состояние банкомата не корректно");
    }

    public AtmInitStateIsWrongException(Throwable ex) {
        super("Начальное состояние банкомата не корректно", ex);
    }
}

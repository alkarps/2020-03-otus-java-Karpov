package my.alkarps.department.model.exception;

/**
 * @author alkarps
 * create date 30.07.2020 12:44
 */
public abstract class DepartmentException extends RuntimeException {
    public DepartmentException(String message) {
        super(message);
    }

    public DepartmentException(String message, Throwable ex) {
        super(message, ex);
    }
}

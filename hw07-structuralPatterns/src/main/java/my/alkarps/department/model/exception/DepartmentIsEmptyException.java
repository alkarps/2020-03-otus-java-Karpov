package my.alkarps.department.model.exception;

/**
 * @author alkarps
 * create date 30.07.2020 14:15
 */
public class DepartmentIsEmptyException extends DepartmentException {
    public DepartmentIsEmptyException() {
        super("Департамент не содержит банкоматов");
    }
}

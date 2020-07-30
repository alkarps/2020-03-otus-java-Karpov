package my.alkarps.department.model.exception;

/**
 * @author alkarps
 * create date 30.07.2020 12:46
 */
public class AtmNotAvailableForWorkException extends DepartmentException {
    public AtmNotAvailableForWorkException() {
        super("Данный департамент не пригоден к эксплуатации");
    }
}

package my.alkarps.department.model.exception;

/**
 * @author alkarps
 * create date 30.07.2020 12:49
 */
public class AtmIsEmptyException extends DepartmentException {
    public AtmIsEmptyException() {
        super("Банкомат не имеет денежных средств для выдачи клиентам");
    }
}

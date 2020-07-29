package my.alkarps.atm.model.exception;

/**
 * @author alkarps
 * create date 27.07.2020 10:24
 */
public class CashBoxIsEmptyException extends AtmException {
    public CashBoxIsEmptyException() {
        super("Касса не должна быть пустой");
    }
}

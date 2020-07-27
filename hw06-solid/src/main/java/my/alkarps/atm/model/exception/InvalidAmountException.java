package my.alkarps.atm.model.exception;

/**
 * @author alkarps
 * create date 27.07.2020 12:38
 */
public class InvalidAmountException extends AtmException {
    public InvalidAmountException() {
        super("Некорректная сумма");
    }
}

package my.alkarps.atm.model.exception;

/**
 * @author alkarps
 * create date 27.07.2020 9:26
 */
public class CashBoxStateIsWrongException extends AtmException {
    public CashBoxStateIsWrongException() {
        super("Неверное состояние кассы");
    }
}

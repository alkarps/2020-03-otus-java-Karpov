package my.alkarps.atm.model.exception;

/**
 * @author alkarps
 * create date 27.07.2020 14:02
 */
public class NotEnoughBanknotesException extends AtmException {
    public NotEnoughBanknotesException() {
        super("Недостаточно банкнот для выдачи средств");
    }
}

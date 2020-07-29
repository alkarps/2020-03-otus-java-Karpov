package my.alkarps.atm.model.exception;

/**
 * @author alkarps
 * create date 28.07.2020 13:36
 */
public class InvalidBanknotesCountException extends AtmException {
    public InvalidBanknotesCountException() {
        super("Некорректное количество банкнот");
    }
}

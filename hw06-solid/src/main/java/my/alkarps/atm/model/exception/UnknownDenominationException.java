package my.alkarps.atm.model.exception;

/**
 * @author alkarps
 * create date 27.07.2020 13:21
 */
public class UnknownDenominationException extends AtmException {
    public UnknownDenominationException() {
        super("Получены нераспознанные банкноты.");
    }
}

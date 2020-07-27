package my.alkarps.atm.model.exception;

/**
 * @author alkarps
 * create date 27.07.2020 9:11
 */
public class DenominationNotInitialException extends AtmException {
    public DenominationNotInitialException() {
        super("Номинал кассеты не указан.");
    }
}

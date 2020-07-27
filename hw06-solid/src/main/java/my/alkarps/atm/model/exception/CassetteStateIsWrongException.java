package my.alkarps.atm.model.exception;

/**
 * @author alkarps
 * create date 27.07.2020 9:12
 */
public class CassetteStateIsWrongException extends AtmException {

    public CassetteStateIsWrongException() {
        super("Неверное состояние кассеты");
    }

    public CassetteStateIsWrongException(Throwable ex) {
        super("Неверное состояние кассеты", ex);
    }
}

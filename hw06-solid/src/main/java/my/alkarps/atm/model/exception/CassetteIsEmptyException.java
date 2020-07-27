package my.alkarps.atm.model.exception;

/**
 * @author alkarps
 * create date 27.07.2020 10:26
 */
public class CassetteIsEmptyException extends AtmException{
    public CassetteIsEmptyException() {
        super("Кассета пуста");
    }
}

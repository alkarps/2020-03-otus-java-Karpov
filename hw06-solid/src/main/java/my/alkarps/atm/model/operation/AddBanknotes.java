package my.alkarps.atm.model.operation;

import my.alkarps.atm.model.Denomination;

import java.util.Map;

/**
 * @author alkarps
 * create date 27.07.2020 12:34
 */
public interface AddBanknotes {
    void addBanknotes(Map<Denomination, Long> banknotes);
}

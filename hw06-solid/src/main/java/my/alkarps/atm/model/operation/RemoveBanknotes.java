package my.alkarps.atm.model.operation;

import my.alkarps.atm.model.Denomination;

import java.util.Map;

/**
 * @author alkarps
 * create date 27.07.2020 13:46
 */
public interface RemoveBanknotes {
    Map<Denomination, Long> removeBanknotes(long amount);
}

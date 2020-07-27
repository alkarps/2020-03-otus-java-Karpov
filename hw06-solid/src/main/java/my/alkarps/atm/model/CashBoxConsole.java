package my.alkarps.atm.model;

import my.alkarps.atm.model.operation.AddBanknotes;
import my.alkarps.atm.model.operation.CurrentAmount;
import my.alkarps.atm.model.operation.RemoveBanknotes;

/**
 * Представляет собой совокупность интерфейсных операций, доступных для работы над кассой.
 *
 * @author alkarps
 * create date 27.07.2020 11:24
 */
public interface CashBoxConsole extends CurrentAmount, AddBanknotes, RemoveBanknotes {
}

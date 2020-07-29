package my.alkarps.atm.model.operation;

/**
 * Представляет собой совокупность интерфейсных операций, доступных для работы над кассой.
 *
 * @author alkarps
 * create date 27.07.2020 11:24
 */
public interface CashBoxOperation extends CurrentAmount, AddBanknotes, RemoveBanknotes, Empty {
}

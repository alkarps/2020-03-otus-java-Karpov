package my.alkarps.atm;

import my.alkarps.atm.model.operation.CashBoxOperation;
import my.alkarps.atm.model.exception.CashBoxIsEmptyException;
import my.alkarps.atm.model.operation.UserOperation;

/**
 * В данный момент является проксей для кассы.
 * При добавлении валюты - тут будет проще всего реализована логику выбора валютной кассы.
 *
 * @author alkarps
 * create date 22.07.2020 12:38
 */
public class Atm implements UserOperation {
    private final CashBoxOperation cashBox;

    private Atm(CashBoxOperation cashBox) {
        this.cashBox = cashBox;
    }

    @Override
    public long getCurrentAmount() {
        return cashBox.getCurrentAmount();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void addBanknotes(long amount) {
        cashBox.addBanknotes(amount);
    }

    @Override
    public void removeBanknotes(long amount) {

    }

    public static class Builder {
        private CashBoxOperation cashBox;

        private Builder() {
        }

        public Builder cashBox(CashBoxOperation cashBox) {
            this.cashBox = cashBox;
            return this;
        }

        public Atm build() {
            if (cashBox == null || cashBox.isEmpty()) {
                throw new CashBoxIsEmptyException();
            }
            return new Atm(cashBox);
        }
    }
}

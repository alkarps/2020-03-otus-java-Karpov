package my.alkarps.atm;

import my.alkarps.atm.model.CashBox;
import my.alkarps.atm.model.CashBoxConsole;
import my.alkarps.atm.model.exception.CashBoxIsEmptyException;

/**
 * В данный момент является проксей для кассы.
 * При добавлении валюты - тут будет проще всего реализована логику выбора валютной кассы.
 *
 * @author alkarps
 * create date 22.07.2020 12:38
 */
public class Atm implements CashBoxConsole {
    private final CashBox cashBox;

    private Atm(CashBox cashBox) {
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
        private CashBox cashbox;

        private Builder() {
        }

        public Builder cashBox(CashBox cashBox) {
            this.cashbox = cashBox;
            return this;
        }

        public Atm build() {
            if (cashbox == null || cashbox.isEmpty()) {
                throw new CashBoxIsEmptyException();
            }
            return new Atm(cashbox);
        }
    }
}

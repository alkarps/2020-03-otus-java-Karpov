package my.alkarps.atm;

import my.alkarps.atm.model.AbonentConsole;
import my.alkarps.atm.model.CashBox;
import my.alkarps.atm.model.exception.CashBoxIsEmptyException;

/**
 * @author alkarps
 * create date 22.07.2020 12:38
 */
public class Atm implements AbonentConsole {
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

package my.alkarps.atm;

import my.alkarps.atm.model.Cashbox;
import my.alkarps.atm.operation.CurrentAmount;

import java.math.BigInteger;

/**
 * @author alkarps
 * create date 22.07.2020 12:38
 */
public class Atm implements CurrentAmount {
    private Cashbox cashbox;

    private Atm(Cashbox cashbox) {
        this.cashbox = cashbox;
    }

    @Override
    public BigInteger getCurrentAmount() {
        return cashbox.getCurrentAmount();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Cashbox cashbox;

        private Builder() {
        }

        public Builder cashbox(Cashbox cashbox) {
            this.cashbox = cashbox;
            return this;
        }

        public Atm build() {
            if (cashbox == null || cashbox.isEmpty()) {
                throw new RuntimeException("Касса пуста");
            }
            return new Atm(cashbox);
        }
    }
}

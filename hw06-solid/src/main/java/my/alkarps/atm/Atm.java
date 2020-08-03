package my.alkarps.atm;

import my.alkarps.atm.model.Denomination;
import my.alkarps.atm.model.exception.CashBoxIsEmptyException;
import my.alkarps.atm.model.operation.CashBoxOperation;
import my.alkarps.atm.model.operation.DepartmentOperation;
import my.alkarps.atm.model.operation.UserOperation;

import java.util.Map;

import static my.alkarps.atm.util.Utils.isNullOrEmpty;
import static my.alkarps.atm.util.Utils.throwExceptionIfTrue;

/**
 * В данный момент является проксей для кассы.
 * При добавлении валюты - тут будет проще всего реализована логику выбора валютной кассы.
 *
 * @author alkarps
 * create date 22.07.2020 12:38
 */
public class Atm implements UserOperation, DepartmentOperation {
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
    public void addBanknotes(Map<Denomination, Long> banknotes) {
        cashBox.addBanknotes(banknotes);
    }

    @Override
    public Map<Denomination, Long> removeBanknotes(long amount) {
        return cashBox.removeBanknotes(amount);
    }

    @Override
    public String backup() {
        return cashBox.backup();
    }

    @Override
    public void restore(String restoringState) {
        cashBox.restore(restoringState);
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
            throwExceptionIfTrue(isNullOrEmpty(cashBox), CashBoxIsEmptyException::new);
            return new Atm(cashBox);
        }
    }
}

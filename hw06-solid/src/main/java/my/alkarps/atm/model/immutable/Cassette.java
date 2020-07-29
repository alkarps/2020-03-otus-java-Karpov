package my.alkarps.atm.model.immutable;

import lombok.EqualsAndHashCode;
import my.alkarps.atm.model.Denomination;
import my.alkarps.atm.model.exception.CassetteStateIsWrongException;
import my.alkarps.atm.model.exception.DenominationNotInitialException;
import my.alkarps.atm.model.memento.BackupState;
import my.alkarps.atm.model.operation.Clone;
import my.alkarps.atm.model.operation.CurrentAmount;
import my.alkarps.atm.model.operation.Empty;

import java.util.function.Supplier;

import static com.google.common.base.Strings.isNullOrEmpty;
import static my.alkarps.atm.util.Utils.throwExceptionIfTrue;

/**
 * @author alkarps
 * create date 22.07.2020 13:52
 */
@EqualsAndHashCode
public class Cassette implements CurrentAmount, Empty, BackupState, Clone<Cassette> {
    private static final String DELIMITER = ":";
    private final Denomination denomination;
    private final long count;

    private Cassette(Denomination denomination, long count) {
        this.denomination = denomination;
        this.count = count;
    }

    private Cassette(String restoringState) {
        throwIfNotValidState(isNullOrEmpty(restoringState) ||
                !restoringState.contains(DELIMITER));
        String[] state = restoringState.split(DELIMITER);
        throwIfNotValidState(state.length != 2);
        this.denomination = Denomination.fromName(state[0])
                .orElseThrow(CassetteStateIsWrongException::new);
        try {
            this.count = Long.parseLong(state[1]);
        } catch (NumberFormatException ex) {
            throw new CassetteStateIsWrongException(ex);
        }
        throwIfNotValidState(count < 0);
    }

    private void throwIfNotValidState(boolean check) {
        throwExceptionIfTrue(check, CassetteStateIsWrongException::new);
    }

    @Override
    public long getCurrentAmount() {
        return count * denomination.getAmount();
    }

    @Override
    public boolean isEmpty() {
        return count <= 0;
    }

    @Override
    public String backup() {
        return denomination.name() + DELIMITER + count;
    }

    @Override
    public Cassette cloneThis() {
        return new Cassette(this.denomination, this.count);
    }

    public Denomination getDenomination() {
        return this.denomination;
    }

    public Cassette removeBanknotes(long count) {
        return changeBanknotes(() -> this.count - Math.min(count, this.count));
    }

    public Cassette addBanknotes(long count) {
        return changeBanknotes(() -> this.count + count);
    }

    private Cassette changeBanknotes(Supplier<Long> supplier) {
        return builder().denomination(this.denomination).count(supplier.get()).build();
    }

    public long getCount() {
        return this.count;
    }

    static Cassette restore(String restoringState) {
        return new Cassette(restoringState);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static int compare(Cassette c1, Cassette c2) {
        return -1 * Denomination.compare(getDenomination(c1), getDenomination(c2));
    }

    private static Denomination getDenomination(Cassette c) {
        return c == null ? null : c.getDenomination();
    }

    public static class Builder {
        private long count = 0L;
        private Denomination denomination;

        private Builder() {
        }

        public Builder denomination(Denomination denomination) {
            this.denomination = denomination;
            return this;
        }

        public Builder count(long count) {
            this.count = count > 0 ? count : 0;
            return this;
        }

        public Cassette build() {
            throwExceptionIfTrue(denomination == null, DenominationNotInitialException::new);
            return new Cassette(denomination, count);
        }
    }
}

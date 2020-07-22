package my.alkarps.atm.model;

import my.alkarps.atm.operation.CurrentAmount;
import my.alkarps.atm.operation.Empty;

import java.math.BigInteger;

/**
 * @author alkarps
 * create date 22.07.2020 13:52
 */
public class Cassette implements CurrentAmount, Empty {
    private long count;
    private final Denomination denomination;

    private Cassette(Denomination denomination, long count) {
        this.denomination = denomination;
        this.count = count;
    }

    @Override
    public BigInteger getCurrentAmount() {
        return BigInteger.valueOf(count).multiply(denomination.getAmount());
    }

    @Override
    public boolean isEmpty() {
        return count <= 0;
    }

    public Denomination getDenomination() {
        return this.denomination;
    }

    public void removeBone(long count) {
        this.count -= count;
    }

    public void addBone(long count) {
        this.count -= count;
    }

    public long getCount() {
        return this.count;
    }

    public static Builder builder() {
        return new Builder();
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
            if (denomination == null) {
                throw new RuntimeException("Номинал кассеты не указан.");
            }
            return new Cassette(denomination, count);
        }
    }
}

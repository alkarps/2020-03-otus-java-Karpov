package my.alkarps.atm.model;

import com.google.common.collect.ImmutableList;
import my.alkarps.atm.operation.CurrentAmount;
import my.alkarps.atm.operation.Empty;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author alkarps
 * create date 22.07.2020 14:35
 */
public class Cashbox implements CurrentAmount, Empty {
    private final List<Cassette> cassettes;

    private Cashbox(List<Cassette> cassettes) {
        this.cassettes = ImmutableList.copyOf(cassettes);
    }

    @Override
    public BigInteger getCurrentAmount() {
        return cassettes.stream()
                .map(Cassette::getCurrentAmount)
                .reduce(BigInteger::add)
                .orElse(BigInteger.ZERO);
    }

    @Override
    public boolean isEmpty() {
        return cassettes.isEmpty() || cassettes.stream().allMatch(Cassette::isEmpty);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<Cassette> cassettes = new ArrayList<>();

        private Builder() {
        }

        public Builder addCassettes(Cassette cassette) {
            if (cassette == null || cassette.isEmpty()) {
                throw new RuntimeException("Кассета пуста");
            }
            Optional<Cassette> exist = this.cassettes.stream()
                    .filter(_cassette -> _cassette.getDenomination().equals(cassette.getDenomination()))
                    .findAny();
            if (exist.isPresent()) {
                exist.get().addBone(cassette.getCount());
            } else {
                this.cassettes.add(cassette);
            }
            return this;
        }

        public Cashbox build() {
            if (cassettes.isEmpty()) {
                throw new RuntimeException("Касса не должна быть пустой");
            }
            return new Cashbox(cassettes);
        }
    }
}

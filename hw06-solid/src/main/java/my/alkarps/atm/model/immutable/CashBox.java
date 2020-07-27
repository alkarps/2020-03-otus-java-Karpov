package my.alkarps.atm.model.immutable;

import my.alkarps.atm.model.exception.*;
import my.alkarps.atm.model.memento.BackupState;
import my.alkarps.atm.model.memento.RestoreState;
import my.alkarps.atm.model.operation.CashBoxOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Реализация кассы на основе иммутабельной кассеты.
 *
 * @author alkarps
 * create date 22.07.2020 14:35
 */
public class CashBox implements CashBoxOperation, BackupState, RestoreState {
    private static final String DELIMITER = ";";
    private List<Cassette> cassettes;

    private CashBox(List<Cassette> cassettes) {
        updateCassettesWithSort(cassettes);
    }

    private void updateCassettesWithSort(List<Cassette> cassettes) {
        cassettes.sort(Cassette::compare);
        this.cassettes = cassettes;
    }

    @Override
    public long getCurrentAmount() {
        return cassettes.stream()
                .map(Cassette::getCurrentAmount)
                .reduce(Long::sum)
                .orElse(0L);
    }

    @Override
    public boolean isEmpty() {
        return cassettes.isEmpty() || cassettes.stream().allMatch(Cassette::isEmpty);
    }

    @Override
    public String backup() {
        return cassettes.stream()
                .map(Cassette::backup)
                .collect(Collectors.joining(DELIMITER));
    }

    @Override
    public void restore(String restoringState) {
        if (isNullOrEmpty(restoringState) || restoringState.isBlank()) {
            throw new CashBoxStateIsWrongException();
        }
        this.cassettes = Stream.of(restoringState.split(DELIMITER))
                .map(Cassette::restore)
                .sorted(Cassette::compare)
                .collect(Collectors.toList());
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void addBanknotes(long amount) {
        if (amount > 0) {
            List<Cassette> _cassettes = new ArrayList<>();
            for (Cassette cassette : cassettes) {
                long banknotes = amount / cassette.getDenomination().getAmount();
                _cassettes.add(cassette.addBanknotes(banknotes));
                amount = amount - (banknotes * cassette.getDenomination().getAmount());
            }
            if (amount != 0) {
                throw new UnknownDenominationException();
            }
            updateCassettesWithSort(_cassettes);
        } else {
            throw new InvalidAmountException();
        }
    }

    @Override
    public long removeBanknotes(long amount) {
        if (amount > 0) {
            List<Cassette> _cassettes = new ArrayList<>();
            long removingBanknotes = 0L;
            if (amount <= getCurrentAmount()) {
                for (Cassette cassette : cassettes) {
                    long banknotes = amount / cassette.getDenomination().getAmount();
                    Cassette newState = cassette.removeBanknotes(banknotes);
                    long _removingBanknotes = cassette.getCount() - newState.getCount();
                    removingBanknotes += _removingBanknotes;
                    _cassettes.add(newState);
                    amount = amount - (_removingBanknotes * cassette.getDenomination().getAmount());
                }
            }
            if (amount != 0) {
                throw new NotEnoughBanknotesException();
            }
            updateCassettesWithSort(_cassettes);
            return removingBanknotes;
        } else {
            throw new InvalidAmountException();
        }
    }

    public static class Builder {
        private final List<Cassette> cassettes = new ArrayList<>();

        private Builder() {
        }

        public Builder addCassettes(Cassette cassette) {
            if (cassette == null || cassette.isEmpty()) {
                throw new CassetteIsEmptyException();
            }
            //TODO проверить на необходимость копирования
            this.cassettes.add(cassette);
            return this;
        }

        public CashBox build() {
            if (cassettes.isEmpty()) {
                throw new CashBoxIsEmptyException();
            }
            return new CashBox(cassettes);
        }
    }
}

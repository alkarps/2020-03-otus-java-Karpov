package my.alkarps.atm.model.immutable;

import my.alkarps.atm.model.Denomination;
import my.alkarps.atm.model.exception.*;
import my.alkarps.atm.model.memento.BackupState;
import my.alkarps.atm.model.memento.RestoreState;
import my.alkarps.atm.model.operation.CashBoxOperation;
import my.alkarps.atm.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Strings.isNullOrEmpty;
import static my.alkarps.atm.util.Utils.throwExceptionIfTrue;

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
        throwExceptionIfTrue(isNullOrEmpty(restoringState) || restoringState.isBlank(),
                CashBoxStateIsWrongException::new);
        this.cassettes = Stream.of(restoringState.split(DELIMITER))
                .map(Cassette::restore)
                .sorted(Cassette::compare)
                .collect(Collectors.toList());
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void addBanknotes(Map<Denomination, Long> banknotes) {
        if (banknotes != null && !banknotes.isEmpty()) {
            List<Cassette> _cassettes = new ArrayList<>();
            for (Cassette cassette : cassettes) {
                Long _banknotes = banknotes.getOrDefault(cassette.getDenomination(), 0L);
                throwExceptionIfTrue(_banknotes == null || _banknotes < 0, InvalidBanknotesCountException::new);
                _cassettes.add(cassette.addBanknotes(_banknotes));
                banknotes.remove(cassette.getDenomination());
            }
            throwExceptionIfTrue(!banknotes.isEmpty(), UnknownDenominationException::new);
            updateCassettesWithSort(_cassettes);
        }
    }

    @Override
    public long removeBanknotes(long amount) {
        throwExceptionIfTrue(amount <= 0, InvalidAmountException::new);
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
        throwExceptionIfTrue(amount != 0, NotEnoughBanknotesException::new);
        updateCassettesWithSort(_cassettes);
        return removingBanknotes;
    }

    public static class Builder {
        private final List<Cassette> cassettes = new ArrayList<>();

        private Builder() {
        }

        public Builder addCassettes(Cassette cassette) {
            throwExceptionIfTrue(Utils.isNullOrEmpty(cassette), CassetteIsEmptyException::new);
            //TODO проверить на необходимость копирования
            this.cassettes.add(cassette);
            return this;
        }

        public CashBox build() {
            throwExceptionIfTrue(cassettes.isEmpty(), CashBoxIsEmptyException::new);
            return new CashBox(cassettes);
        }
    }
}

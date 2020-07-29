package my.alkarps.atm.model.immutable;

import my.alkarps.atm.model.Denomination;
import my.alkarps.atm.model.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author alkarps
 * create date 27.07.2020 10:23
 */
class CashBoxTest {

    private final Cassette cassetteWith100b = getCassette(Denomination.b100, 1000);
    private final Cassette cassetteWith500b = getCassette(Denomination.b500, 500);

    private static Cassette getCassette(Denomination denomination, long count) {
        return Cassette.builder()
                .denomination(denomination)
                .count(count)
                .build();
    }

    private CashBox cashBox;

    @BeforeEach
    void setUp() {
        cashBox = CashBox.builder()
                .addCassettes(cassetteWith100b)
                .addCassettes(cassetteWith500b)
                .build();
    }

    @Test
    void builderIsNotNull() {
        assertNotNull(CashBox.builder());
    }

    @Test
    void builder_whenCashBoxIsEmpty_thenThrowCashBoxIsEmptyException() {
        assertThatCode(() -> CashBox.builder().build())
                .isInstanceOf(CashBoxIsEmptyException.class)
                .hasMessage("Касса не должна быть пустой");
    }

    @ParameterizedTest
    @MethodSource("cassetteIsNullOrEmpty")
    void builder_whenCassetteIsNullOrEmpty_thenThrowCassetteIsEmptyException(Cassette cassette) {
        assertThatCode(() -> CashBox.builder().addCassettes(cassette))
                .isInstanceOf(CassetteIsEmptyException.class)
                .hasMessage("Кассета пуста");
    }

    private static Stream<Cassette> cassetteIsNullOrEmpty() {
        return Stream.of(null, getCassette(Denomination.b1000, 0));
    }

    @Test
    void builder_whenAddTwoCassetteWithSameDenomination_thenSumCount() {
        assertThat(CashBox.builder()
                .addCassettes(cassetteWith100b)
                .addCassettes(cassetteWith500b)
                .build())
                .isInstanceOf(CashBox.class)
                .extracting("cassettes")
                .asList()
                .hasSize(2)
                .containsOnly(cassetteWith100b, cassetteWith500b);
    }

    @Test
    void builder_whenAddTwoCassetteWithDifferentDenomination_thenContainTwoCassetteInCashBox() {
        assertThat(CashBox.builder()
                .addCassettes(cassetteWith100b)
                .addCassettes(cassetteWith100b)
                .build())
                .isInstanceOf(CashBox.class)
                .extracting("cassettes")
                .asList()
                .hasSize(2)
                .contains(cassetteWith100b, cassetteWith100b);
    }

    @Test
    void getCurrentAmount() {
        long currentAmount = cassetteWith100b.getCurrentAmount() + cassetteWith500b.getCurrentAmount();
        assertThat(cashBox.getCurrentAmount()).isEqualByComparingTo(currentAmount);
    }

    @Test
    void isEmpty_whenCurrentAmountIsEqualZero_thenReturnTrue() {
        cashBox.restore("b1:0");
        assertThat(cashBox.isEmpty()).isTrue();
    }

    @Test
    void isEmpty_whenCurrentAmountIsNotEqualZero_thenReturnFalse() {
        assertThat(cashBox.isEmpty()).isFalse();
    }

    @Test
    void backup() {
        final String expected = "b500:500;b100:1000";
        assertThat(cashBox.backup()).isNotBlank().isEqualTo(expected);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = " ")
    void restore_whenRestoringStateIsNullOrBlank_thenThrowCashBoxStateIsWrongException(String restoringState) {
        assertThatCode(() -> cashBox.restore(restoringState))
                .isInstanceOf(CashBoxStateIsWrongException.class)
                .hasMessage("Неверное состояние кассы");
    }

    @ParameterizedTest
    @ValueSource(strings = {"asda", "asda:dasd", "wsa:", ":", "b1:-1", "b1:-1;", "b1:100;daw:2"})
    void restore_whenRestoringStateIsWrong_thenThrowCashBoxStateIsWrongException(String restoringState) {
        assertThatCode(() -> cashBox.restore(restoringState))
                .isInstanceOf(CassetteStateIsWrongException.class)
                .hasMessage("Неверное состояние кассеты");
    }

    @ParameterizedTest
    @MethodSource("validRestoringState")
    void restore_whenRestoringStateIsValid_thenRestoringCashBoxState(String restoringState, Cassette[] cassettes) {
        assertThatCode(() -> cashBox.restore(restoringState)).doesNotThrowAnyException();
        assertThat(cashBox)
                .isInstanceOf(CashBox.class)
                .extracting("cassettes")
                .asList()
                .hasSize(cassettes.length)
                .containsOnlyElementsOf(asList(cassettes));
    }

    private static Stream<Arguments> validRestoringState() {
        return Stream.of(
                createArgumentsForSuccessRestoring(Cassette.restore("b1:0")),
                createArgumentsForSuccessRestoring(Cassette.restore("b1:1000")),
                createArgumentsForSuccessRestoring(Cassette.restore("b10:10"),
                        Cassette.restore("b10:101")),
                createArgumentsForSuccessRestoring(Cassette.restore("b10:10"),
                        Cassette.restore("b100:101"))
        );
    }

    private static Arguments createArgumentsForSuccessRestoring(Cassette... cassettes) {
        String restoringState = Stream.of(cassettes).map(Cassette::backup).collect(Collectors.joining(";"));
        return Arguments.of(restoringState, cassettes);
    }

    @ParameterizedTest
    @NullSource
    @MethodSource("emptyMapWithBanknotes")
    void addBanknotes_whenAmountIsWrong_thenThrowInvalidAmountException(Map<Denomination, Long> banknotes) {
        assertThatCode(() -> cashBox.addBanknotes(banknotes))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("banknotesWithUnknownDenominationForAdd")
    void addBanknotes_whenCashBoxNotHaveDenomination_thenThrowUnknownDenominationException(Map<Denomination, Long> banknotes) {
        assertThatCode(() -> cashBox.addBanknotes(banknotes))
                .isInstanceOf(UnknownDenominationException.class)
                .hasMessage("Получены нераспознанные банкноты.");
        assertThat(cashBox).isInstanceOf(CashBox.class)
                .extracting("cassettes")
                .asList()
                .hasSize(2)
                .containsOnlyElementsOf(asList(cassetteWith500b, cassetteWith100b));
    }

    @ParameterizedTest
    @MethodSource("banknotesWithKnownDenominationForAdd")
    void addBanknotes_whenCashBoxHaveAllDenomination_thenDoNothing(Map<Denomination, Long> banknotes) {
        Cassette cassetteWith500bAfter = addBanknotesInCassette(cassetteWith500b, banknotes);
        Cassette cassetteWith100bAfter = addBanknotesInCassette(cassetteWith100b, banknotes);
        assertThatCode(() -> cashBox.addBanknotes(banknotes)).doesNotThrowAnyException();
        assertThat(cashBox).isInstanceOf(CashBox.class)
                .extracting("cassettes")
                .asList()
                .hasSize(2)
                .containsOnlyElementsOf(asList(cassetteWith500bAfter, cassetteWith100bAfter));
    }

    @ParameterizedTest
    @MethodSource("banknotesWithIncorrectCountForAdd")
    void addBanknotes_whenCountIsNullOrLessZero_thenThrowInvalidBanknotesCountException(Map<Denomination, Long> banknotes) {
        assertThatCode(() -> cashBox.addBanknotes(banknotes))
                .isInstanceOf(InvalidBanknotesCountException.class)
                .hasMessage("Некорректное количество банкнот");
        assertThat(cashBox).isInstanceOf(CashBox.class)
                .extracting("cassettes")
                .asList()
                .hasSize(2)
                .containsOnlyElementsOf(asList(cassetteWith500b, cassetteWith100b));
    }

    private Cassette addBanknotesInCassette(Cassette cassette, Map<Denomination, Long> banknotes) {
        return cassette.addBanknotes(banknotes.getOrDefault(cassette.getDenomination(), 0L));
    }

    private static Stream<Arguments> emptyMapWithBanknotes() {
        return Stream.of(Arguments.of(new HashMap<Denomination, Long>()));
    }

    private static Stream<Arguments> banknotesWithUnknownDenominationForAdd() {
        return Stream.of(
                Arguments.of(createBanknotes(Denomination.b1, 100L)),
                Arguments.of(createBanknotes(Denomination.b2, 10L)),
                Arguments.of(addBanknotesWithNewDenomination(createBanknotes(Denomination.b100, 100L), Denomination.b2, 1L))
        );
    }

    private static Stream<Arguments> banknotesWithIncorrectCountForAdd() {
        return Stream.of(
                Arguments.of(createBanknotes(Denomination.b100, null)),
                Arguments.of(createBanknotes(Denomination.b500, -10L))
        );
    }

    private static Stream<Arguments> banknotesWithKnownDenominationForAdd() {
        return Stream.of(
                Arguments.of(createBanknotes(Denomination.b100, 100L)),
                Arguments.of(createBanknotes(Denomination.b500, 10L)),
                Arguments.of(addBanknotesWithNewDenomination(createBanknotes(Denomination.b100, 100L), Denomination.b500, 3L))
        );
    }

    private static Map<Denomination, Long> createBanknotes(Denomination key, Long value) {
        Map<Denomination, Long> banknotes = new HashMap<>();
        banknotes.put(key, value);
        return banknotes;
    }

    private static Map<Denomination, Long> addBanknotesWithNewDenomination(Map<Denomination, Long> banknotes, Denomination newKey, Long newValue) {
        banknotes.put(newKey, newValue);
        return banknotes;
    }

    @ParameterizedTest
    @ValueSource(longs = {-1, 0})
    void removeBanknotes_whenAmountIsWrong_thenThrowInvalidAmountException(long amount) {
        assertThatCode(() -> cashBox.removeBanknotes(amount))
                .isInstanceOf(InvalidAmountException.class)
                .hasMessage("Некорректная сумма");
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 123, 1000_000_000})
    void removeBanknotes_whenCashBoxNotHavEnoughBanknotes_thenThrowNotEnoughBanknotesException(long amount) {
        assertThatCode(() -> cashBox.removeBanknotes(amount))
                .isInstanceOf(NotEnoughBanknotesException.class)
                .hasMessage("Недостаточно банкнот для выдачи средств");
        assertThat(cashBox).isInstanceOf(CashBox.class)
                .extracting("cassettes")
                .asList()
                .hasSize(2)
                .containsOnlyElementsOf(asList(cassetteWith500b, cassetteWith100b));
    }

    @ParameterizedTest
    @ValueSource(longs = {4300, 100})
    void removeBanknotes_whenCashBoxHaveAllDenomination_thenReturnBanknotes(long amount) {
        Map<Denomination, Long> returningBanknotes = new HashMap<>();
        Cassette new500bState = returnNewCassetteStateAndUpdateReturningBanknotes(cassetteWith500b, returningBanknotes, amount / 500);
        Cassette new100bState = returnNewCassetteStateAndUpdateReturningBanknotes(cassetteWith100b, returningBanknotes, (amount % 500) / 100);
        assertThat(cashBox.removeBanknotes(amount)).isEqualTo(returningBanknotes);
        assertThat(cashBox).isInstanceOf(CashBox.class)
                .extracting("cassettes")
                .asList()
                .hasSize(2)
                .containsOnlyElementsOf(asList(new500bState, new100bState));
    }

    private Cassette returnNewCassetteStateAndUpdateReturningBanknotes(Cassette oldState, Map<Denomination, Long> returningBanknotes, long count) {
        Cassette newState = removeBanknotes(oldState, count);
        updateReturningBanknotesIfChangeCassette(oldState, newState, returningBanknotes);
        return newState;
    }

    private Cassette removeBanknotes(Cassette cassette, long count) {
        return cassette.removeBanknotes(count);
    }

    private void updateReturningBanknotesIfChangeCassette(Cassette oldState, Cassette newState, Map<Denomination, Long> returningBanknotes) {
        long count = oldState.getCount() - newState.getCount();
        if (count != 0) {
            returningBanknotes.put(oldState.getDenomination(), count);
        }
    }
}
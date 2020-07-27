package my.alkarps.atm.model;

import my.alkarps.atm.model.exception.CassetteStateIsWrongException;
import my.alkarps.atm.model.exception.DenominationNotInitialException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author alkarps
 * create date 27.07.2020 9:06
 */
class CassetteTest {
    private final Denomination denomination = Denomination.b100;
    private final long count = 1000;

    private Cassette testCassette;

    @BeforeEach
    public void setUp() {
        testCassette = Cassette.builder()
                .denomination(denomination)
                .count(count)
                .build();
    }

    @Test
    void builder_whenCallBuilder_thenReturnNotNullInstance() {
        assertNotNull(Cassette.builder());
    }

    @Test
    void builder_whenTryBuildEmptyCassette_thenCatchDenominationNotInitialException() {
        assertThatCode(() -> Cassette.builder().build())
                .isInstanceOf(DenominationNotInitialException.class)
                .hasMessage("Номинал кассеты не указан.");
    }

    @Test
    void builder_whenTryBuildCassetteWithoutDenomination_thenCatchDenominationNotInitialException() {
        long count = 0;
        assertThatCode(() -> Cassette.builder().count(count).build())
                .isInstanceOf(DenominationNotInitialException.class)
                .hasMessage("Номинал кассеты не указан.");
    }

    @Test
    void builder_whenTryBuildCassetteWithoutAnyBanknotes_thenCatchDenominationNotInitialException() {
        Denomination denomination = Denomination.b1;
        long count = 0;
        assertThat(Cassette.builder().denomination(denomination).build())
                .isInstanceOf(Cassette.class)
                .hasFieldOrPropertyWithValue("denomination", denomination)
                .hasFieldOrPropertyWithValue("count", count);
    }

    @Test
    void builder_whenTryBuildCassette_thenCatchDenominationNotInitialException() {
        Denomination denomination = Denomination.b1;
        long count = 2L;
        assertThat(Cassette.builder().denomination(denomination).count(count).build())
                .isInstanceOf(Cassette.class)
                .hasFieldOrPropertyWithValue("denomination", denomination)
                .hasFieldOrPropertyWithValue("count", count);
    }

    @Test
    void getCurrentAmount() {
        Long expected = denomination.getAmount() * count;
        assertThat(testCassette.getCurrentAmount())
                .isNotNull()
                .isNotNegative()
                .isEqualByComparingTo(expected);
    }

    @Test
    void isEmpty_whenCassetteIsEmpty_returnTrue() {
        Cassette emptyCassette = Cassette.builder().denomination(denomination).build();
        assertThat(emptyCassette.isEmpty())
                .isTrue();
    }

    @Test
    void isEmpty_whenCassetteIsNotEmpty_returnFalse() {
        assertThat(testCassette.isEmpty()).isFalse();
    }

    @Test
    void backup() {
        final String expected = "b100:1000";
        assertThat(testCassette.backup()).isNotBlank().isEqualTo(expected);
    }

    @Test
    void getDenomination() {
        assertThat(testCassette.getDenomination()).isEqualTo(denomination);
    }

    @Test
    void removeBanknotes() {
        long removingBanknotes = 100;
        assertThat(testCassette.removeBanknotes(removingBanknotes))
                .isNotEqualTo(testCassette)
                .hasFieldOrPropertyWithValue("denomination", testCassette.getDenomination())
                .hasFieldOrPropertyWithValue("count", count - removingBanknotes);
    }

    @Test
    void addBanknotes() {
        long addingBanknotes = 100;
        assertThat(testCassette.addBanknotes(addingBanknotes))
                .isNotEqualTo(testCassette)
                .hasFieldOrPropertyWithValue("denomination", testCassette.getDenomination())
                .hasFieldOrPropertyWithValue("count", count + addingBanknotes);
    }

    @Test
    void getCount() {
        assertThat(testCassette.getCount()).isEqualTo(count);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"asda", "asda:dasd", "wsa:", ":", "b1:-1"})
    void restore_whenRestoringStateIsWrong_thenThrowCassetteStateIsWrongException(String restoringState) {
        assertThatCode(() -> Cassette.restore(restoringState))
                .isInstanceOf(CassetteStateIsWrongException.class)
                .hasMessage("Неверное состояние кассеты");
    }

    @Test
    void restore_whenRestoringStateHasWrongCount_thenThrowCassetteStateIsWrongException() {
        String restoringState = "b1:sda";
        assertThatCode(() -> Cassette.restore(restoringState))
                .isInstanceOf(CassetteStateIsWrongException.class)
                .hasCauseInstanceOf(NumberFormatException.class)
                .hasMessage("Неверное состояние кассеты");
    }

    @ParameterizedTest
    @MethodSource("restoringCassetteState")
    void restore_whenRestoringStateIsValid_thenReturnCassette(String restoringState, Denomination denomination, Long count) {
        assertThat(Cassette.restore(restoringState))
                .isInstanceOf(Cassette.class)
                .hasFieldOrPropertyWithValue("denomination", denomination)
                .hasFieldOrPropertyWithValue("count", count);
    }

    private static Stream<Arguments> restoringCassetteState() {
        return Stream.of(
                Arguments.of("b1:0", Denomination.b1, 0L),
                Arguments.of("b50:10", Denomination.b50, 10L)
        );
    }
}
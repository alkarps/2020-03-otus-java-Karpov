package my.alkarps.atm;

import my.alkarps.atm.model.Denomination;
import my.alkarps.atm.model.exception.CashBoxIsEmptyException;
import my.alkarps.atm.model.operation.CashBoxOperation;
import my.alkarps.atm.model.operation.UserOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * @author alkarps
 * create date 28.07.2020 14:41
 */
class AtmTest {

    private CashBoxOperation cashBoxOperation;
    private UserOperation atm;

    @BeforeEach
    void setUp() {
        cashBoxOperation = mock(CashBoxOperation.class);
        doReturn(false).when(cashBoxOperation).isEmpty();
        atm = Atm.builder().cashBox(cashBoxOperation).build();
    }

    @Test
    void builder() {
        assertNotNull(Atm.builder());
    }

    @Test
    void builder_whenEmptyBuild_thenThrowCashBoxIsEmptyException() {
        assertThatCode(() -> Atm.builder().build())
                .isInstanceOf(CashBoxIsEmptyException.class)
                .hasMessage("Касса не должна быть пустой");
    }

    @ParameterizedTest
    @NullSource
    @MethodSource("buildWithEmptyCashBox")
    void builder_whenBuildWithEmptyCashBox_thenThrowCashBoxIsEmptyException(CashBoxOperation cashBox) {
        assertThatCode(() -> Atm.builder().cashBox(cashBox).build())
                .isInstanceOf(CashBoxIsEmptyException.class)
                .hasMessage("Касса не должна быть пустой");
        if (cashBox != null) {
            verify(cashBox).isEmpty();
        }
    }

    @Test
    void builder_whenBuildWithFillCashBox_thenReturnAtm() {
        CashBoxOperation cashBoxOperation = mock(CashBoxOperation.class);
        doReturn(false).when(cashBoxOperation).isEmpty();
        assertThat(Atm.builder().cashBox(cashBoxOperation).build())
                .isInstanceOf(Atm.class)
                .hasFieldOrPropertyWithValue("cashBox", cashBoxOperation);
        verify(cashBoxOperation).isEmpty();
    }

    private static Stream<Arguments> buildWithEmptyCashBox() {
        CashBoxOperation cashBoxOperation = mock(CashBoxOperation.class);
        doReturn(true).when(cashBoxOperation).isEmpty();
        return Stream.of(Arguments.of(cashBoxOperation));
    }

    @Test
    void getCurrentAmount_whenCall_thenCallCashBox() {
        long amount = 100L;
        doReturn(amount).when(cashBoxOperation).getCurrentAmount();
        assertThat(atm.getCurrentAmount()).isEqualTo(amount);
        verify(cashBoxOperation).isEmpty();
        verify(cashBoxOperation).getCurrentAmount();
        verify(cashBoxOperation, never()).removeBanknotes(anyLong());
        verify(cashBoxOperation, never()).addBanknotes(any());
    }

    @Test
    void addBanknotes_whenCall_thenCallCashBox() {
        Map<Denomination, Long> addBanknotes = new HashMap<>();
        doNothing().when(cashBoxOperation).addBanknotes(anyMap());
        assertThatCode(() -> atm.addBanknotes(addBanknotes)).doesNotThrowAnyException();
        verify(cashBoxOperation).isEmpty();
        verify(cashBoxOperation).addBanknotes(addBanknotes);
        verify(cashBoxOperation, never()).getCurrentAmount();
        verify(cashBoxOperation, never()).removeBanknotes(anyLong());
    }

    @Test
    void removeBanknotes_whenCall_thenCallCashBox() {
        Map<Denomination, Long> addBanknotes = new HashMap<>();
        long amount = 100L;
        doReturn(addBanknotes).when(cashBoxOperation).removeBanknotes(anyLong());
        assertThat(atm.removeBanknotes(amount)).isEqualTo(addBanknotes);
        verify(cashBoxOperation).isEmpty();
        verify(cashBoxOperation).removeBanknotes(amount);
        verify(cashBoxOperation, never()).getCurrentAmount();
        verify(cashBoxOperation, never()).addBanknotes(any());
    }
}
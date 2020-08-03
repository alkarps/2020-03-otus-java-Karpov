package my.alkarps;

import my.alkarps.atm.Atm;
import my.alkarps.atm.model.Denomination;
import my.alkarps.atm.model.immutable.CashBox;
import my.alkarps.atm.model.immutable.Cassette;
import my.alkarps.atm.model.operation.CashBoxOperation;
import my.alkarps.department.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static my.alkarps.atm.model.Denomination.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author alkarps
 * create date 01.08.2020 12:46
 */
public class IntegrationTest {
    private Department department;
    private Atm firstAtm;
    private Atm secondAtm;

    @BeforeEach
    protected void setUp() {
        department = new Department();
        createFirstAtm();
        createSecondAtm();
    }

    private void createFirstAtm() {
        Cassette firstCassette = createCassette(b10, 100L);
        Cassette secondCassette = createCassette(b100, 1000L);
        CashBoxOperation cashBox = createCashBox(firstCassette, secondCassette);
        firstAtm = createSpyAtm(cashBox);
    }

    private void createSecondAtm() {
        Cassette firstCassette = createCassette(b5, 200L);
        Cassette secondCassette = createCassette(b500, 200L);
        CashBoxOperation cashBox = createCashBox(firstCassette, secondCassette);
        secondAtm = createSpyAtm(cashBox);
    }

    private Atm createSpyAtm(CashBoxOperation cashBox) {
        return spy(Atm.builder().cashBox(cashBox).build());
    }

    private CashBoxOperation createCashBox(Cassette... cassettes) {
        CashBox.Builder builder = CashBox.builder();
        for (Cassette cassette : cassettes) {
            builder.addCassettes(cassette);
        }
        return builder.build();
    }

    private Cassette createCassette(Denomination denomination, long count) {
        return Cassette.builder()
                .denomination(denomination)
                .count(count)
                .build();
    }

    @Test
    void demo() {
        long initAmount = firstAtm.getCurrentAmount() + secondAtm.getCurrentAmount();
        String firstAtmBackup = firstAtm.backup();
        String secondAtmBackup = secondAtm.backup();

        department.addNewAtm(firstAtm);
        department.addNewAtm(secondAtm);
        verifyCurrentAmount(initAmount);

        removeBanknotes(initAmount, firstAtm, 100L);
        removeBanknotes(initAmount, secondAtm, 500L);
        addBanknotes(initAmount, firstAtm, b10, 1);
        addBanknotes(initAmount, secondAtm, b500, 1);
        
        verify(firstAtm, times(2)).backup();
        verify(secondAtm, times(2)).backup();
        verify(firstAtm, times(4)).restore(firstAtmBackup);
        verify(secondAtm, times(4)).restore(secondAtmBackup);
    }

    private void verifyCurrentAmount(long initAmount) {
        assertThat(department.getCurrentAmount())
                .isNotNull()
                .isEqualTo(initAmount);
    }

    private void removeBanknotes(long initAmount, Atm atm, long removingMoney) {
        atm.removeBanknotes(removingMoney);
        verifyCurrentAmount(initAmount - removingMoney);
        resetWithCheck(initAmount);
    }

    private void addBanknotes(long initAmount, Atm atm, Denomination denomination, long count) {
        Map<Denomination, Long> addBanknotes = new HashMap<>();
        addBanknotes.put(denomination, count);
        atm.addBanknotes(addBanknotes);
        verifyCurrentAmount(initAmount + denomination.getAmount() * count);
        resetWithCheck(initAmount);
    }

    private void resetWithCheck(long initAmount) {
        resetAtms();
        verifyCurrentAmount(initAmount);
    }

    private void resetAtms() {
        assertThat(department.resetAllAtm())
                .isNotNull()
                .hasFieldOrPropertyWithValue("allCount", 2L)
                .hasFieldOrPropertyWithValue("successRestoring", 2L);
    }
}

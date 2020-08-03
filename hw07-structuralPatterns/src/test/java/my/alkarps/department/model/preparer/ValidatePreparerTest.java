package my.alkarps.department.model.preparer;

import my.alkarps.department.model.AtmOperation;
import my.alkarps.department.model.exception.AtmIsEmptyException;
import my.alkarps.department.model.exception.AtmNotAvailableForWorkException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

/**
 * @author alkarps
 * create date 30.07.2020 13:33
 */
class ValidatePreparerTest {

    private Preparer preparer;
    private AtmOperation atm;

    @BeforeEach
    void setUp() {
        atm = mock(AtmOperation.class);
        preparer = spy(new ValidatePreparer(null));
    }

    @Test
    void preparer_whenAtmIsNull_thenThrowAtmNotAvailableForWorkException() {
        assertThatCode(() -> preparer.preparer(null))
                .isInstanceOf(AtmNotAvailableForWorkException.class)
                .hasMessage("Данный департамент не пригоден к эксплуатации");
        verify(atm, never()).isValid();
        verify(atm, never()).getCurrentAmount();
        verify(preparer, never()).nextPreparer(atm);
        verify(atm, never()).backup();
        verify(atm, never()).restoreInitState();
    }

    @Test
    void preparer_whenAtmIsNotValid_thenThrowAtmNotAvailableForWorkException() {
        doReturn(false).when(atm).isValid();
        assertThatCode(() -> preparer.preparer(atm))
                .isInstanceOf(AtmNotAvailableForWorkException.class)
                .hasMessage("Данный департамент не пригоден к эксплуатации");
        verify(atm).isValid();
        verify(atm, never()).getCurrentAmount();
        verify(preparer, never()).nextPreparer(atm);
        verify(atm, never()).backup();
        verify(atm, never()).restoreInitState();
    }

    @Test
    void preparer_whenAtmNotHaveBanknotes_thenThrowAtmIsEmptyException() {
        doReturn(true).when(atm).isValid();
        doReturn(0L).when(atm).getCurrentAmount();
        assertThatCode(() -> preparer.preparer(atm))
                .isInstanceOf(AtmIsEmptyException.class)
                .hasMessage("Банкомат не имеет денежных средств для выдачи клиентам");
        verify(atm).isValid();
        verify(atm).getCurrentAmount();
        verify(preparer, never()).nextPreparer(atm);
        verify(atm, never()).backup();
        verify(atm, never()).restoreInitState();
    }

    @Test
    void preparer_whenAtmIsValid_thenCallNextPreparer() {
        doReturn(true).when(atm).isValid();
        doReturn(1L).when(atm).getCurrentAmount();
        doReturn(atm).when(preparer).nextPreparer(any());
        assertThat(preparer.preparer(atm)).isEqualTo(atm);
        verify(atm).isValid();
        verify(atm).getCurrentAmount();
        verify(preparer).nextPreparer(atm);
        verify(atm, never()).backup();
        verify(atm, never()).restoreInitState();
    }
}
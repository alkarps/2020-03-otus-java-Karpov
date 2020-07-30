package my.alkarps.department.model.preparer;

import my.alkarps.department.model.AtmOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

/**
 * @author alkarps
 * create date 30.07.2020 15:35
 */
class InitAtmStatePreparerTest {

    private Preparer preparer;
    private AtmOperation atm;

    @BeforeEach
    void setUp() {
        atm = mock(AtmOperation.class);
        preparer = spy(new InitAtmStatePreparer(null));
    }

    @Test
    void preparer() {
        doNothing().when(atm).backup();
        assertThatCode(() -> preparer.preparer(atm)).doesNotThrowAnyException();
        verify(atm).backup();
        verify(preparer).nextPreparer(atm);
        verify(atm, never()).isValid();
        verify(atm, never()).restoreInitState();
        verify(atm, never()).getCurrentAmount();
    }
}
package my.alkarps.department.model.preparer;

import my.alkarps.department.model.AtmOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**s
 * @author alkarps
 * create date 30.07.2020 13:34
 */
class PreparerTest {

    private Preparer preparer;
    private AtmOperation atm;

    @BeforeEach
    void setUp() {
        atm = mock(AtmOperation.class);
        preparer = mock(Preparer.class, CALLS_REAL_METHODS);
    }

    @Test
    void preparer_whenCall_whenReturnInputParams() {
        doReturn(atm).when(preparer).preparer(any());
        assertThat(preparer.preparer(atm))
                .isNotNull()
                .isEqualTo(atm);
        verify(preparer).preparer(atm);
    }

    @Test
    void nextPreparer_whenNotHaveNextPreparer_thenReturnInputParam() {
        assertThat(preparer.nextPreparer(atm)).isEqualTo(atm);
        verify(preparer, never()).preparer(any());
    }

    @Test
    void nextPreparer_whenHaveNextPreparer_thenCallMethodPreparerWithInputParam() {
        setNextPreparer();
        doReturn(atm).when(preparer).preparer(any());
        assertThat(preparer.nextPreparer(atm)).isEqualTo(atm);
        verify(preparer).preparer(any());
    }

    private void setNextPreparer() {
        try {
            Field field = Preparer.class.getDeclaredField("next");
            FieldSetter.setField(preparer, field, preparer);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
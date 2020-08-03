package my.alkarps.department.model;

import my.alkarps.department.model.exception.AtmInitStateIsWrongException;
import my.alkarps.atm.model.operation.DepartmentOperation;
import my.alkarps.department.model.AtmOperation;
import my.alkarps.department.model.AtmWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

/**
 * @author alkarps
 * create date 30.07.2020 13:09
 */
class AtmWrapperTest {

    private DepartmentOperation departmentOperation;
    private AtmOperation wrapper;

    @BeforeEach
    public void setUp() {
        departmentOperation = mock(DepartmentOperation.class);
        wrapper = AtmWrapper.wrap(departmentOperation);
    }

    @Test
    void wrap() {
        assertThat(AtmWrapper.wrap(departmentOperation))
                .isNotNull()
                .hasFieldOrPropertyWithValue("restoringState", null)
                .hasFieldOrPropertyWithValue("atm", departmentOperation);
    }

    @Test
    void restoreInitState_whenInitStateNotSave_thenThrowAtmInitStateIsWrongException() {
        assertThat(wrapper).hasFieldOrPropertyWithValue("restoringState", null);
        assertThatCode(() -> wrapper.restoreInitState())
                .isInstanceOf(AtmInitStateIsWrongException.class)
                .hasMessage("Начальное состояние банкомата не корректно")
                .hasNoCause();
        verify(departmentOperation, never()).backup();
        verify(departmentOperation, never()).restore(any());
        verify(departmentOperation, never()).getCurrentAmount();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void restoreInitState_whenBackupSaveNullOrEmptyState_thenThrowAtmInitStateIsWrongException(String state) {
        doReturn(state).when(departmentOperation).backup();
        assertThatCode(() -> wrapper.backup()).doesNotThrowAnyException();
        assertThat(wrapper).hasFieldOrPropertyWithValue("restoringState", state);
        assertThatCode(() -> wrapper.restoreInitState())
                .isInstanceOf(AtmInitStateIsWrongException.class)
                .hasMessage("Начальное состояние банкомата не корректно")
                .hasNoCause();
        verify(departmentOperation, times(1)).backup();
        verify(departmentOperation, never()).restore(any());
        verify(departmentOperation, never()).getCurrentAmount();
    }

    @Test
    void restoreInitState_whenRestoringStateIsValid_thenCallDepartmentOperationAsIs() {
        String state = "restoringState";
        doReturn(state).when(departmentOperation).backup();
        assertThatCode(() -> wrapper.backup()).doesNotThrowAnyException();
        assertThat(wrapper).hasFieldOrPropertyWithValue("restoringState", state);
        doNothing().when(departmentOperation).restore(any());
        assertThatCode(() -> wrapper.restoreInitState())
                .doesNotThrowAnyException();
        verify(departmentOperation, times(1)).backup();
        verify(departmentOperation).restore(state);
        verify(departmentOperation, never()).getCurrentAmount();
    }

    @Test
    void restoreInitState_whenRestoringStateIsWrong_thenThrowAtmInitStateIsWrongExceptionWithCause() {
        String state = "restoringState";
        doReturn(state).when(departmentOperation).backup();
        assertThatCode(() -> wrapper.backup()).doesNotThrowAnyException();
        assertThat(wrapper).hasFieldOrPropertyWithValue("restoringState", state);
        doThrow(new NullPointerException()).when(departmentOperation).restore(any());
        assertThatCode(() -> wrapper.restoreInitState())
                .isInstanceOf(AtmInitStateIsWrongException.class)
                .hasMessage("Начальное состояние банкомата не корректно")
                .hasCauseInstanceOf(NullPointerException.class);
        verify(departmentOperation, times(1)).backup();
        verify(departmentOperation).restore(state);
        verify(departmentOperation, never()).getCurrentAmount();
    }

    @Test
    void backup_whenCall_thenCallDepartmentOperationAndSaveState() {
        String restoringState = "backup";
        doReturn(restoringState).when(departmentOperation).backup();
        assertThatCode(() -> wrapper.backup()).doesNotThrowAnyException();
        assertThat(wrapper).hasFieldOrPropertyWithValue("restoringState", restoringState);
        verify(departmentOperation).backup();
        verify(departmentOperation, never()).restore(any());
        verify(departmentOperation, never()).getCurrentAmount();

    }

    @Test
    void getCurrentAmount() {
        long amount = 1000L;
        doReturn(amount).when(departmentOperation).getCurrentAmount();
        assertThat(wrapper.getCurrentAmount()).isEqualTo(amount);
        verify(departmentOperation).getCurrentAmount();
        verify(departmentOperation, never()).backup();
        verify(departmentOperation, never()).restore(any());
    }

    @Test
    void isValid_whenDepartmentOperationIsNull_thenReturnFalse() {
        assertThat(AtmWrapper.wrap(null).isValid()).isFalse();
    }

    @Test
    void isValid_whenDepartmentOperationIsNotNull_thenReturnTrue() {
        assertThat(AtmWrapper.wrap(null).isValid()).isFalse();
    }
}
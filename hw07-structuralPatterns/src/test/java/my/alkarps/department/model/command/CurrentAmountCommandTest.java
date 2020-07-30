package my.alkarps.department.model.command;

import my.alkarps.department.model.AtmOperation;
import my.alkarps.department.model.exception.DepartmentIsEmptyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

/**
 * @author alkarps
 * create date 30.07.2020 16:11
 */
class CurrentAmountCommandTest {

    private AtmOperation atm;
    private Command<Long> command;

    @BeforeEach
    void setUp() {
        atm = mock(AtmOperation.class);
        command = new CurrentAmountCommand();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void execute_whenListAtmIsNullOrEmpty_thenThrowDepartmentIsEmptyException(List<AtmOperation> list) {
        assertThatCode(() -> command.execute(list))
                .isInstanceOf(DepartmentIsEmptyException.class)
                .hasMessage("Департамент не содержит банкоматов");
        verify(atm, never()).getCurrentAmount();
        verify(atm, never()).isValid();
        verify(atm, never()).restoreInitState();
        verify(atm, never()).backup();
    }

    @Test
    void execute_whenListAtmHasAtm_thenReturnCurrentAmount() {
        long count = 199;
        doReturn(count).when(atm).getCurrentAmount();
        assertThat(command.execute(singletonList(atm)))
                .isNotNull()
                .isEqualTo(count);
        verify(atm, times(1)).getCurrentAmount();
        verify(atm, never()).isValid();
        verify(atm, never()).restoreInitState();
        verify(atm, never()).backup();
    }
}
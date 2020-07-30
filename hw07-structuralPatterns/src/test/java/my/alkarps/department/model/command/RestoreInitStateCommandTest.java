package my.alkarps.department.model.command;

import my.alkarps.department.model.AtmOperation;
import my.alkarps.department.model.exception.AtmInitStateIsWrongException;
import my.alkarps.department.model.exception.DepartmentIsEmptyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.verification.VerificationMode;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

/**
 * @author alkarps
 * create date 30.07.2020 16:19
 */
class RestoreInitStateCommandTest {

    private AtmOperation atm;
    private Command<RestoreInitStateCommand.Statistic> command;

    @BeforeEach
    void setUp() {
        atm = mock(AtmOperation.class);
        command = new RestoreInitStateCommand();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void execute_whenListAtmIsNullOrEmpty_thenThrowDepartmentIsEmptyException(List<AtmOperation> list) {
        assertThatCode(() -> command.execute(list))
                .isInstanceOf(DepartmentIsEmptyException.class)
                .hasMessage("Департамент не содержит банкоматов");
        verifyAtm(atm, never());
    }

    @Test
    void execute_whenListAtmHasAtmAndSuccessRestore_thenStatisticsWithSuccessRestoring() {
        setSuccessCall(atm);
        assertThat(command.execute(singletonList(atm)))
                .isInstanceOf(RestoreInitStateCommand.Statistic.class)
                .hasFieldOrPropertyWithValue("allCount", 1L)
                .hasFieldOrPropertyWithValue("successRestoring", 1L);
        verifyAtm(atm, times(1));
    }

    @Test
    void execute_whenListAtmHasAtmAndFailRestore_thenStatisticsWithoutSuccessRestoring() {
        setThrowException();
        assertThat(command.execute(singletonList(atm)))
                .isInstanceOf(RestoreInitStateCommand.Statistic.class)
                .hasFieldOrPropertyWithValue("allCount", 1L)
                .hasFieldOrPropertyWithValue("successRestoring", 0L);
        verifyAtm(atm, times(1));
    }

    @Test
    void execute_whenListAtmHasTwoAtmsWithFailAndSuccessRestore_thenStatisticsHasOnlyOneSuccessRestoring() {
        setThrowException();
        AtmOperation atm2 = mock(AtmOperation.class);
        setSuccessCall(atm2);
        assertThat(command.execute(asList(atm, atm2)))
                .isInstanceOf(RestoreInitStateCommand.Statistic.class)
                .hasFieldOrPropertyWithValue("allCount", 2L)
                .hasFieldOrPropertyWithValue("successRestoring", 1L);
        verifyAtm(atm, times(1));
        verifyAtm(atm2, times(1));
    }

    private void setSuccessCall(AtmOperation atm) {
        doNothing().when(atm).restoreInitState();
    }

    private void setThrowException() {
        doThrow(new AtmInitStateIsWrongException()).when(atm).restoreInitState();
    }

    private void verifyAtm(AtmOperation atm, VerificationMode times) {
        verify(atm, times).restoreInitState();
        verify(atm, never()).getCurrentAmount();
        verify(atm, never()).isValid();
        verify(atm, never()).backup();
    }
}
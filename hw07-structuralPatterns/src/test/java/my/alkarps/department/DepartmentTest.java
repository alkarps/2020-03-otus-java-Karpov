package my.alkarps.department;

import my.alkarps.atm.model.operation.DepartmentOperation;
import my.alkarps.department.model.AtmOperation;
import my.alkarps.department.model.command.CurrentAmountCommand;
import my.alkarps.department.model.command.RestoreInitStateCommand;
import my.alkarps.department.model.preparer.InitAtmStatePreparer;
import my.alkarps.department.model.preparer.Preparer;
import my.alkarps.department.model.preparer.ValidatePreparer;
import my.alkarps.department.model.watcher.Watcher;
import my.alkarps.department.model.watcher.WatcherManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static my.alkarps.department.model.AtmWrapper.wrap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author alkarps
 * create date 30.07.2020 16:29
 */
class DepartmentTest {
    private Preparer preparerChain;
    private Watcher watcher;
    private Department department;

    @BeforeEach
    void setUp() {
        preparerChain = mock(Preparer.class);
        watcher = mock(Watcher.class);
        department = new Department(preparerChain, watcher);
    }

    @Test
    void addNewAtm() {
        DepartmentOperation newAtm = mock(DepartmentOperation.class);
        AtmOperation atm = wrap(newAtm);
        doReturn(atm).when(preparerChain).preparer(any());
        doNothing().when(watcher).addNewAtm(any());
        assertThatCode(() -> department.addNewAtm(newAtm)).doesNotThrowAnyException();
        verify(preparerChain).preparer(atm);
        verify(watcher).addNewAtm(atm);
        verify(watcher, never()).sendCommandOnAtm(any());
    }

    @Test
    void resetAllAtm() {
        RestoreInitStateCommand.Statistic statistic = new RestoreInitStateCommand.Statistic(1, 1);
        doReturn(statistic).when(watcher).sendCommandOnAtm(any());
        assertThat(department.resetAllAtm())
                .isInstanceOf(RestoreInitStateCommand.Statistic.class)
                .isEqualTo(statistic);
        verify(watcher).sendCommandOnAtm(argThat(argument -> argument != null &&
                argument.getClass().equals(RestoreInitStateCommand.class)));
        verify(watcher, never()).addNewAtm(any());
        verify(preparerChain, never()).preparer(any());
    }

    @Test
    void getCurrentAmount() {
        long count = 123L;
        doReturn(count).when(watcher).sendCommandOnAtm(any());
        assertThat(department.getCurrentAmount())
                .isNotNull()
                .isEqualTo(count);
        verify(watcher).sendCommandOnAtm(argThat(argument -> argument != null &&
                argument.getClass().equals(CurrentAmountCommand.class)));
        verify(watcher, never()).addNewAtm(any());
        verify(preparerChain, never()).preparer(any());
    }

    @Test
    public void defaultInitHasCorrectInit() {
        Department department = new Department();
        assertThat(department)
                .hasNoNullFieldsOrProperties()
                .extracting("watcher")
                .isInstanceOf(WatcherManager.class);
        assertThat(department)
                .extracting("preparerChain")
                .isInstanceOf(ValidatePreparer.class)
                .hasNoNullFieldsOrProperties()
                .extracting("next")
                .isInstanceOf(InitAtmStatePreparer.class)
                .hasAllNullFieldsOrProperties();
    }
}
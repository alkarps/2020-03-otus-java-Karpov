package my.alkarps.department.model.watcher;

import my.alkarps.department.model.AtmOperation;
import my.alkarps.department.model.command.Command;
import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author alkarps
 * create date 30.07.2020 15:58
 */
class WatcherManagerTest {

    private AtmOperation atm;
    private Watcher watcher;
    private Command<String> command;

    @BeforeEach
    void setUp() {
        atm = mock(AtmOperation.class);
        watcher = new WatcherManager();
        command = mock(Command.class);
    }

    @Test
    void addNewAtm() {
        baseAssertWatcher()
                .isEmpty();
        assertThatCode(() -> watcher.addNewAtm(atm)).doesNotThrowAnyException();
        baseAssertWatcher()
                .hasSize(1)
                .containsOnly(atm);
    }

    private AbstractListAssert<?, List<?>, Object, ObjectAssert<Object>> baseAssertWatcher() {
        return assertThat(watcher)
                .hasFieldOrProperty("atms")
                .extracting("atms")
                .isNotNull()
                .asList();
    }

    @Test
    void sendCommandOnAtm() {
        baseAssertWatcher()
                .isEmpty();
        assertThatCode(() -> watcher.addNewAtm(atm)).doesNotThrowAnyException();
        baseAssertWatcher()
                .hasSize(1)
                .containsOnly(atm);
        String expectedString = "expectedString";
        doReturn(expectedString).when(command).execute(any());
        assertThat(watcher.sendCommandOnAtm(command))
                .isNotNull()
                .isEqualTo(expectedString);
        verify(command).execute(argThat(argument -> argument != null && argument.size() == 1 && argument.contains(atm)));
    }
}
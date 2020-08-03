package my.alkarps.department.model.watcher;

import my.alkarps.department.model.AtmOperation;
import my.alkarps.department.model.command.Command;

import java.util.ArrayList;
import java.util.List;

/**
 * @author alkarps
 * create date 30.07.2020 15:49
 */
public class WatcherManager implements Watcher {
    private final List<AtmOperation> atms;

    public WatcherManager() {
        this.atms = new ArrayList<>();
    }

    @Override
    public void addNewAtm(AtmOperation atm) {
        atms.add(atm);
    }

    @Override
    public <T> T sendCommandOnAtm(Command<T> command) {
        return command.execute(this.atms);
    }
}

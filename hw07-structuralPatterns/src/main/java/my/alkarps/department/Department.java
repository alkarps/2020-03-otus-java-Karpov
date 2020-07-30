package my.alkarps.department;

import my.alkarps.atm.model.operation.DepartmentOperation;
import my.alkarps.department.model.AtmOperation;
import my.alkarps.department.model.AtmWrapper;
import my.alkarps.department.model.command.CurrentAmountCommand;
import my.alkarps.department.model.command.RestoreInitStateCommand;
import my.alkarps.department.model.preparer.InitAtmStatePreparer;
import my.alkarps.department.model.preparer.Preparer;
import my.alkarps.department.model.preparer.ValidatePreparer;
import my.alkarps.department.model.watcher.Watcher;
import my.alkarps.department.model.watcher.WatcherManager;

/**
 * @author alkarps
 * create date 29.07.2020 17:28
 */
public class Department {
    private final Preparer preparerChain;
    private final Watcher watcher;

    public Department() {
        InitAtmStatePreparer initAtmStatePreparer = new InitAtmStatePreparer(null);
        this.preparerChain = new ValidatePreparer(initAtmStatePreparer);
        this.watcher = new WatcherManager();
    }

    public Department(Preparer preparerChain, Watcher watcher) {
        this.preparerChain = preparerChain;
        this.watcher = watcher;
    }

    public void addNewAtm(DepartmentOperation newAtm) {
        AtmOperation validAtm = preparerChain.preparer(AtmWrapper.wrap(newAtm));
        watcher.addNewAtm(validAtm);
    }

    public RestoreInitStateCommand.Statistic resetAllAtm() {
        return watcher.sendCommandOnAtm(new RestoreInitStateCommand());
    }

    public Long getCurrentAmount() {
        return watcher.sendCommandOnAtm(new CurrentAmountCommand());
    }
}

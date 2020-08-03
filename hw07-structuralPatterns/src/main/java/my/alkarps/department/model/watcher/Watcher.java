package my.alkarps.department.model.watcher;

import my.alkarps.department.model.AtmOperation;
import my.alkarps.department.model.command.Command;

/**
 * @author alkarps
 * create date 30.07.2020 15:48
 */
public interface Watcher {
    void addNewAtm(AtmOperation atm);

    <T> T sendCommandOnAtm(Command<T> command);
}

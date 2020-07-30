package my.alkarps.atm.model.operation;

import my.alkarps.atm.model.memento.BackupState;
import my.alkarps.atm.model.memento.RestoreState;

/**
 * @author alkarps
 * create date 30.07.2020 12:00
 */
public interface DepartmentOperation extends CurrentAmount, BackupState, RestoreState {
}

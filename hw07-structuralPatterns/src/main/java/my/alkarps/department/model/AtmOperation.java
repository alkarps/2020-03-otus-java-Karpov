package my.alkarps.department.model;

import my.alkarps.atm.model.operation.CurrentAmount;

/**
 * @author alkarps
 * create date 30.07.2020 12:38
 */
public interface AtmOperation extends CurrentAmount {
    void backup();

    void restoreInitState();

    boolean isValid();
}

package my.alkarps.department.model.command;

import my.alkarps.department.model.AtmOperation;

import java.util.List;

/**
 * @author alkarps
 * create date 30.07.2020 12:04
 */
public interface Command<T> {
    T execute(List<AtmOperation> atms);
}

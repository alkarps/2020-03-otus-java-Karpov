package my.alkarps.atm.model.memento;

/**
 * @author alkarps
 * create date 26.07.2020 14:42
 */
public interface RestoreState {
    void restore(String restoringState);
}

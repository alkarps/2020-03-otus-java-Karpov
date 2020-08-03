package my.alkarps.department.model.preparer;

import my.alkarps.department.model.AtmOperation;

/**
 * @author alkarps
 * create date 30.07.2020 12:54
 */
public class InitAtmStatePreparer extends Preparer {
    public InitAtmStatePreparer(Preparer next) {
        super(next);
    }

    @Override
    public AtmOperation preparer(AtmOperation newAtm) {
        newAtm.backup();
        return nextPreparer(newAtm);
    }
}

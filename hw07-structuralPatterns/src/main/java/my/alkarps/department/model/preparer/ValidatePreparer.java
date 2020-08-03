package my.alkarps.department.model.preparer;

import my.alkarps.department.model.AtmOperation;
import my.alkarps.department.model.exception.AtmIsEmptyException;
import my.alkarps.department.model.exception.AtmNotAvailableForWorkException;

import static my.alkarps.department.util.Checks.throwExceptionIfTrue;

/**
 * @author alkarps
 * create date 30.07.2020 12:42
 */
public class ValidatePreparer extends Preparer {
    public ValidatePreparer(Preparer next) {
        super(next);
    }

    @Override
    public AtmOperation preparer(AtmOperation newAtm) {
        throwExceptionIfTrue(newAtm == null || !newAtm.isValid(), AtmNotAvailableForWorkException::new);
        throwExceptionIfTrue(newAtm.getCurrentAmount() <= 0, AtmIsEmptyException::new);
        return nextPreparer(newAtm);
    }
}

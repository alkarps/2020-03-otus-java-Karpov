package my.alkarps.department.model;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;
import my.alkarps.atm.model.operation.DepartmentOperation;
import my.alkarps.department.model.exception.AtmInitStateIsWrongException;

import static com.google.common.base.Strings.isNullOrEmpty;
import static my.alkarps.department.util.Checks.throwExceptionIfTrue;

/**
 * @author alkarps
 * create date 30.07.2020 12:37
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class AtmWrapper implements AtmOperation {
    private final DepartmentOperation atm;
    private String restoringState;

    public static AtmOperation wrap(DepartmentOperation atm) {
        return new AtmWrapper(atm);
    }

    @Override
    public void restoreInitState() {
        throwExceptionIfTrue(isNullOrEmpty(restoringState), AtmInitStateIsWrongException::new);
        try {
            atm.restore(restoringState);
        } catch (Exception ex) {
            throw new AtmInitStateIsWrongException(ex);
        }
    }

    @Override
    public boolean isValid() {
        return atm != null;
    }

    @Override
    public void backup() {
        restoringState = atm.backup();
    }

    @Override
    public long getCurrentAmount() {
        return atm.getCurrentAmount();
    }
}

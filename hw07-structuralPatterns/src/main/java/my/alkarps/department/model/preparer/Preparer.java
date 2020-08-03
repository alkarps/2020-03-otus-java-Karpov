package my.alkarps.department.model.preparer;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import my.alkarps.department.model.AtmOperation;

/**
 * @author alkarps
 * create date 30.07.2020 12:25
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Preparer {
    private final Preparer next;

    public abstract AtmOperation preparer(AtmOperation newAtm);

    protected AtmOperation nextPreparer(AtmOperation atmUnit) {
        if (next == null) {
            return atmUnit;
        }
        return next.preparer(atmUnit);
    }
}

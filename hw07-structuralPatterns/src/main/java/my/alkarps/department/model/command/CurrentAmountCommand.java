package my.alkarps.department.model.command;

import my.alkarps.department.model.AtmOperation;
import my.alkarps.department.model.exception.DepartmentIsEmptyException;

import java.util.List;

import static my.alkarps.department.util.Checks.throwExceptionIfTrue;

/**
 * @author alkarps
 * create date 30.07.2020 15:00
 */
public class CurrentAmountCommand implements Command<Long> {
    @Override
    public Long execute(List<AtmOperation> atms) {
        throwExceptionIfTrue(atms == null || atms.isEmpty(), DepartmentIsEmptyException::new);
        return atms.stream()
                .map(AtmOperation::getCurrentAmount)
                .reduce(Long::sum)
                .orElse(0L);
    }
}

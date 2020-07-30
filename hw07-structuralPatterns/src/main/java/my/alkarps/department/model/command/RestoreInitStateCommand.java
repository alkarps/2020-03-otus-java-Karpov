package my.alkarps.department.model.command;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import my.alkarps.department.model.AtmOperation;
import my.alkarps.department.model.exception.DepartmentException;
import my.alkarps.department.model.exception.DepartmentIsEmptyException;

import java.util.List;

import static my.alkarps.department.util.Checks.throwExceptionIfTrue;

/**
 * @author alkarps
 * create date 30.07.2020 14:12
 */
public class RestoreInitStateCommand implements Command<RestoreInitStateCommand.Statistic> {
    @Override
    public Statistic execute(List<AtmOperation> atms) {
        throwExceptionIfTrue(atms == null || atms.isEmpty(), DepartmentIsEmptyException::new);
        Statistic.Builder builder = Statistic.builder().allCount(atms.size());
        atms.forEach(atm -> {
            try {
                atm.restoreInitState();
                builder.addSuccessRestoring();
            } catch (DepartmentException ex) {
                ex.printStackTrace();
            }
        });
        return builder.build();
    }

    @Value
    @RequiredArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Statistic {
        long allCount;
        long successRestoring;

        private static Statistic.Builder builder() {
            return new Builder();
        }

        private static class Builder {
            private long allCount, successRestoring;

            public Builder allCount(long allCount) {
                this.allCount = allCount;
                return this;
            }

            public Builder addSuccessRestoring() {
                this.successRestoring++;
                return this;
            }

            public Statistic build() {
                return new Statistic(allCount, successRestoring);
            }
        }
    }
}

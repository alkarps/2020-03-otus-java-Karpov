package my.alkarps.department.util;

import my.alkarps.department.model.exception.DepartmentException;

import java.util.function.Supplier;

/**
 * @author alkarps
 * create date 30.07.2020 12:48
 */
public class Checks {
    public static <T extends DepartmentException> void throwExceptionIfTrue(boolean check, Supplier<T> supplier) {
        if (check) throw supplier.get();
    }
}

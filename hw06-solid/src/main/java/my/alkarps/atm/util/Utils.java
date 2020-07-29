package my.alkarps.atm.util;

import my.alkarps.atm.model.exception.AtmException;
import my.alkarps.atm.model.operation.Empty;

import java.util.function.Supplier;

/**
 * @author alkarps
 * create date 28.07.2020 13:44
 */
public class Utils {
    public static boolean isNullOrEmpty(Empty empty) {
        return empty == null || empty.isEmpty();
    }

    public static <T extends AtmException> void throwExceptionIfTrue(boolean check, Supplier<T> supplier) {
        if (check) throw supplier.get();
    }
}

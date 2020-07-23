package my.alkarps.engine.helper.notvalid;

import my.alkarps.annotation.AfterAll;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author alkarps
 * create date 17.07.2020 18:15
 */
public class TestClassWithOnlyAfterAllMethods {
    @AfterAll
    public static void setDown() {
        fail("Вызван метод, хотя не должен");
    }

    @AfterAll
    protected static void setDown2() {
        fail("Вызван метод, хотя не должен");
    }

    @AfterAll
    private static void setDown3() {
        fail("Вызван метод, хотя не должен");
    }

    @AfterAll
    static void setDown4() {
        fail("Вызван метод, хотя не должен");
    }
}

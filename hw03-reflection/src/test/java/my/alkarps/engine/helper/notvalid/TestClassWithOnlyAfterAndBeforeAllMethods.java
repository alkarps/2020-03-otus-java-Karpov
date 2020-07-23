package my.alkarps.engine.helper.notvalid;

import my.alkarps.annotation.AfterAll;
import my.alkarps.annotation.BeforeAll;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author alkarps
 * create date 17.07.2020 18:20
 */
public class TestClassWithOnlyAfterAndBeforeAllMethods {
    @BeforeAll
    public static void setUp() {
        fail("Вызван метод, хотя не должен");
    }

    @BeforeAll
    protected static void setUp2() {
        fail("Вызван метод, хотя не должен");
    }

    @BeforeAll
    private static void setUp3() {
        fail("Вызван метод, хотя не должен");
    }

    @BeforeAll
    static void setUp4() {
        fail("Вызван метод, хотя не должен");
    }

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

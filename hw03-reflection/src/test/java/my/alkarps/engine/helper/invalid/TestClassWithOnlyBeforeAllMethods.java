package my.alkarps.engine.helper.invalid;

import my.alkarps.annotation.BeforeAll;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author alkarps
 * create date 17.07.2020 18:13
 */
public class TestClassWithOnlyBeforeAllMethods {
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
}

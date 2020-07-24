package my.alkarps.engine.helper.invalid;

import my.alkarps.annotation.BeforeEach;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author alkarps
 * create date 17.07.2020 18:13
 */
public class TestClassWithOnlyBeforeEachMethods {
    @BeforeEach
    public void setUp() {
        fail("Вызван метод, хотя не должен");
    }

    @BeforeEach
    protected void setUp2() {
        fail("Вызван метод, хотя не должен");
    }

    @BeforeEach
    private void setUp3() {
        fail("Вызван метод, хотя не должен");
    }

    @BeforeEach
    void setUp4() {
        fail("Вызван метод, хотя не должен");
    }
}

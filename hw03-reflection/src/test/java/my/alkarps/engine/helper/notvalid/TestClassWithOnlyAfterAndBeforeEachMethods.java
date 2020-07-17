package my.alkarps.engine.helper.notvalid;

import my.alkarps.annotation.AfterEach;
import my.alkarps.annotation.BeforeEach;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author alkarps
 * create date 17.07.2020 18:20
 */
public class TestClassWithOnlyAfterAndBeforeEachMethods {
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
    @AfterEach
    public void setDown() {
        fail("Вызван метод, хотя не должен");
    }

    @AfterEach
    protected void setDown2() {
        fail("Вызван метод, хотя не должен");
    }

    @AfterEach
    private void setDown3() {
        fail("Вызван метод, хотя не должен");
    }

    @AfterEach
    void setDown4() {
        fail("Вызван метод, хотя не должен");
    }
}

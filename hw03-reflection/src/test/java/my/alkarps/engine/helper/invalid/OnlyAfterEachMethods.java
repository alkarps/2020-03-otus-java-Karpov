package my.alkarps.engine.helper.invalid;

import my.alkarps.annotation.AfterEach;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author alkarps
 * create date 17.07.2020 18:15
 */
public class OnlyAfterEachMethods {
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

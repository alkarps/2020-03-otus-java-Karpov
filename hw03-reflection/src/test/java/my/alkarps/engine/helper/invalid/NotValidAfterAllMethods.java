package my.alkarps.engine.helper.invalid;

import my.alkarps.annotation.AfterAll;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author alkarps
 * create date 23.07.2020 15:56
 */
public class NotValidAfterAllMethods {
    @AfterAll
    public void setDown() {
        fail("Вызван метод, хотя не должен");
    }
}

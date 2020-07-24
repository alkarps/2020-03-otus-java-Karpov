package my.alkarps.engine.helper.invalid;

import my.alkarps.annotation.AfterEach;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author alkarps
 * create date 17.07.2020 18:15
 */
public class TestClassWithNotValidAfterEachMethods {
    @AfterEach
    public static void setDown(){
        fail("Вызван метод, хотя не должен");
    }
}

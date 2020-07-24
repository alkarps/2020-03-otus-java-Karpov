package my.alkarps.engine.helper.invalid;

import my.alkarps.annotation.BeforeAll;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author alkarps
 * create date 17.07.2020 18:10
 */
public class TestClassWithNotValidBeforeAllMethods {
    @BeforeAll
    public void setBeforeEach() {
        fail("Вызван метод, хотя не должен");
    }
}

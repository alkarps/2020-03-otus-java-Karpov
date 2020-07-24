package my.alkarps.engine.helper.fail;

import my.alkarps.annotation.BeforeAll;
import my.alkarps.annotation.Test;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author alkarps
 * create date 23.07.2020 16:22
 */
public class TestClassFailWithBeforeAllMethod {

    @BeforeAll
    public static void setUp() {
        fail("Тут должно упасть");
    }

    @Test
    public void publicMethod() {
        fail("Этого вызова не должно быть");
    }
}

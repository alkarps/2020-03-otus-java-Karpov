package my.alkarps.engine.helper.fail;

import my.alkarps.annotation.BeforeEach;
import my.alkarps.annotation.Test;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author alkarps
 * create date 23.07.2020 7:53
 */
public class TestClassWithTestMethodAndFailBeforeEachMethod {

    @BeforeEach
    private void setUp() {
        fail("Тест на падение before each");
    }

    @Test
    public void publicMethod() {
        System.out.println("this public method");
    }
}

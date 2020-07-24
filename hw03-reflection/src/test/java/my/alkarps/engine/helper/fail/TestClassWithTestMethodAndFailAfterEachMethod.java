package my.alkarps.engine.helper.fail;

import my.alkarps.annotation.AfterEach;
import my.alkarps.annotation.BeforeEach;
import my.alkarps.annotation.Test;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author alkarps
 * create date 23.07.2020 7:59
 */
public class TestClassWithTestMethodAndFailAfterEachMethod {

    @AfterEach
    private void setDown() {
        fail("Тест на падение after each");
    }

    @Test
    public void publicMethod() {
        System.out.println("this public method");
    }
}

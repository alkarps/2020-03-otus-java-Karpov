package my.alkarps.engine.helper.fail;

import my.alkarps.annotation.Test;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author alkarps
 * create date 23.07.2020 7:56
 */
public class TestClassWithFailTestMethod {

    @Test
    public void publicMethod() {
        fail("Этот вызов всегда падает");
    }
}

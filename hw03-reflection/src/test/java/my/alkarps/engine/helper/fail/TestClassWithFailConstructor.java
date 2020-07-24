package my.alkarps.engine.helper.fail;

import my.alkarps.annotation.Test;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author alkarps
 * create date 23.07.2020 9:28
 */
public class TestClassWithFailConstructor {
    public TestClassWithFailConstructor() {
        fail("Всегда падает");
    }

    @Test
    public void test() {
        fail("не должен выполняться");
    }
}

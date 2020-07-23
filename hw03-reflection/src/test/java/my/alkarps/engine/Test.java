package my.alkarps.engine;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author alkarps
 * create date 17.07.2020 18:00
 */
public class Test {
    @BeforeEach
    private void privateSetUp() {
        fail("BeforeEach");
    }

    @org.junit.jupiter.api.Test
    public void test() {
        fail("test");
    }

    @AfterEach
    private void test1() {
        fail("AfterEach");
    }
}

package my.alkarps.engine.helper.fail;

import my.alkarps.annotation.AfterAll;
import my.alkarps.annotation.Test;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author alkarps
 * create date 23.07.2020 16:21
 */
public class FailWithAfterAllMethod {
    @AfterAll
    public static void setDown() {
        fail("Тут должно упасть");
    }

    @Test
    public void publicMethod() {
    }
}

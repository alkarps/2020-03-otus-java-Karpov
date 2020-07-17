package my.alkarps.engine.helper.valid;

import my.alkarps.annotation.AfterEach;
import my.alkarps.annotation.BeforeEach;
import my.alkarps.annotation.Test;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author alkarps
 * create date 17.07.2020 18:27
 */
public class TestClassWithTestAndAfterEachMethods {
    @AfterEach
    public void setDown() {
        System.out.println("setDown");
    }

    @AfterEach
    private void setDown1() {
        System.out.println("setDown1");
    }

    @AfterEach
    void setDown2() {
        System.out.println("setDown2");
    }

    @AfterEach
    private void setDown3() {
        System.out.println("setDown3");
    }

    @Test
    public void publicMethod() {
        System.out.println("this public method");
    }

    @Test
    private void privateMethod() {
        fail("Вызван метод, хотя не должен");
    }

    @Test
    protected void protectedMethod() {
        System.out.println("this protected method");
    }

    @Test
    void packageMethod() {
        System.out.println("this package protected method");
    }
}

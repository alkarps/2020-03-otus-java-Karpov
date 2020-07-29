package my.alkarps.engine.helper.inheritance.without;

import my.alkarps.annotation.AfterEach;
import my.alkarps.annotation.Test;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author alkarps
 * create date 17.07.2020 18:27
 */
public class TestAndAfterEachMethods {
    @AfterEach
    public void publicSetDown() {
        System.out.println("publicSetDown");
    }

    @AfterEach
    protected void protectedSetDown() {
        System.out.println("protectedSetDown");
    }

    @AfterEach
    void packageSetDown() {
        System.out.println("packageSetDown");
    }

    @AfterEach
    private void privateSetDown() {
        System.out.println("privateSetDown");
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

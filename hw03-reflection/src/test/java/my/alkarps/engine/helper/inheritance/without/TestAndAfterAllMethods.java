package my.alkarps.engine.helper.inheritance.without;

import my.alkarps.annotation.AfterAll;
import my.alkarps.annotation.Test;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author alkarps
 * create date 24.07.2020 8:23
 */
public class TestAndAfterAllMethods {
    @AfterAll
    public static void publicSetDownAll() {
        System.out.println("publicSetDownAll");
    }

    @AfterAll
    protected static void protectedSetDownAll() {
        System.out.println("protectedSetDownAll");
    }

    @AfterAll
    static void packageSetDownAll() {
        System.out.println("packageSetDownAll");
    }

    @AfterAll
    private static void privateSetDownAll() {
        System.out.println("privateSetDownAll");
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

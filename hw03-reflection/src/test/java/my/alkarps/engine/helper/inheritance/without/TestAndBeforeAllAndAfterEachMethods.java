package my.alkarps.engine.helper.inheritance.without;

import my.alkarps.annotation.AfterEach;
import my.alkarps.annotation.BeforeAll;
import my.alkarps.annotation.Test;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author alkarps
 * create date 24.07.2020 8:34
 */
public class TestAndBeforeAllAndAfterEachMethods {
    @BeforeAll
    public static void publicSetUpAll() {
        System.out.println("publicSetUpAll");
    }

    @BeforeAll
    protected static void protectedSetUpAll() {
        System.out.println("protectedSetUpAll");
    }

    @BeforeAll
    static void packageSetUpAll() {
        System.out.println("packageSetUpAll");
    }

    @BeforeAll
    private static void privateSetUpAll() {
        System.out.println("privateSetUpAll");
    }

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

package my.alkarps.engine.helper.inheritance.without;

import my.alkarps.annotation.AfterAll;
import my.alkarps.annotation.BeforeEach;
import my.alkarps.annotation.Test;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author alkarps
 * create date 17.07.2020 18:27
 */
public class TestAndBeforeEachAndAfterAllMethods {
    @BeforeEach
    public void publicSetUp() {
        System.out.println("publicSetUp");
    }

    @BeforeEach
    protected void protectedSetUp() {
        System.out.println("protectedSetUp");
    }

    @BeforeEach
    void packageSetUp() {
        System.out.println("packageSetUp");
    }

    @BeforeEach
    private void privateSetUp() {
        System.out.println("privateSetUp");
    }

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

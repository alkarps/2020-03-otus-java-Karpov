package my.alkarps.engine;

import org.junit.jupiter.api.BeforeEach;

/**
 * @author alkarps
 * create date 17.07.2020 18:00
 */
public class Test {
    @BeforeEach
    private void privateSetUp() {
        throw new RuntimeException();
    }

    @org.junit.jupiter.api.Test
    public void test() {
        System.out.println("test");
    }

    @org.junit.jupiter.api.Test
    private void test1() {
        System.out.println("test1");
    }

    @org.junit.jupiter.api.Test
    protected void test2() {
        System.out.println("test2");
    }

    @org.junit.jupiter.api.Test
    void test3() {
        System.out.println("test3");
    }
}

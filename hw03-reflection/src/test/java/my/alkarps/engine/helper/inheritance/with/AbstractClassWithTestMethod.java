package my.alkarps.engine.helper.inheritance.with;

import my.alkarps.annotation.Test;

/**
 * @author alkarps
 * create date 24.07.2020 12:11
 */
public class AbstractClassWithTestMethod {
    @Test
    public void publicMethod() {
        System.out.println("this public method");
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

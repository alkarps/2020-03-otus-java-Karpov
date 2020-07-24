package my.alkarps.engine.helper.inheritance.with;

import my.alkarps.annotation.Test;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author alkarps
 * create date 24.07.2020 12:15
 */
public class OnlyTestMethodWithInheritance extends AbstractClassWithTestMethod {
    @Test
    private void privateMethod() {
        fail("Вызван метод, хотя не должен");
    }
}

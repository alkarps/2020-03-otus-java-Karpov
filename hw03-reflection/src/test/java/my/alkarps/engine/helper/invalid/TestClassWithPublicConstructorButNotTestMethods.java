package my.alkarps.engine.helper.invalid;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author alkarps
 * create date 17.07.2020 17:48
 */
public class TestClassWithPublicConstructorButNotTestMethods {
    public TestClassWithPublicConstructorButNotTestMethods() {
    }

    public void publicMethod(){
        fail("Вызван метод, хотя не должен");
    }

    private void privateMethod(){
        fail("Вызван метод, хотя не должен");
    }

    protected void protectedMethod(){
        fail("Вызван метод, хотя не должен");
    }

    void packageMethod(){
        fail("Вызван метод, хотя не должен");
    }
}

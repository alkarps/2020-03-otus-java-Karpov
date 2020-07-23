package my.alkarps.engine;

import my.alkarps.engine.helper.notvalid.*;
import my.alkarps.engine.helper.valid.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

class EngineTest {

    private Engine engine;

    @BeforeEach
    public void setUp() {
        engine = new Engine();
    }

    @Test
    void run_WhenClassWithPublicConstructorAndTestMethods() {
        assertThatCode(() -> engine.run(TestClassWithPublicConstructorAndTestMethods.class))
                .doesNotThrowAnyException();
    }

    @Test
    void run_WhenClassHasTestAndBeforeEachMethods() {
        assertThatCode(() -> engine.run(TestClassWithTestAndBeforeEachMethods.class))
                .doesNotThrowAnyException();
    }

    @Test
    void run_WhenClassHasTestAndAfterEachMethods() {
        assertThatCode(() -> engine.run(TestClassWithTestAndAfterEachMethods.class))
                .doesNotThrowAnyException();
    }

    @Test
    void run_WhenClassHasTestAndBeforeEachAndAfterEachMethods() {
        assertThatCode(() -> engine.run(TestClassWithTestAndBeforeEachAndAfterEachMethods.class))
                .doesNotThrowAnyException();
    }

    @Test
    void run_WhenClassHasTestMethodAndFailBeforeEachMethod() {
        assertThatCode(() -> engine.run(TestClassWithTestMethodAndFailBeforeEachMethod.class))
                .doesNotThrowAnyException();
    }

    @Test
    void run_WhenClassHasFailTestMethod() {
        assertThatCode(() -> engine.run(TestClassWithFailTestMethod.class))
                .doesNotThrowAnyException();
    }

    @Test
    void run_WhenClassHasTestMethodAndFailAfterEachMethod() {
        assertThatCode(() -> engine.run(TestClassWithTestMethodAndFailAfterEachMethod.class))
                .doesNotThrowAnyException();
    }
}
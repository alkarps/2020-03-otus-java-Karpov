package my.alkarps.engine;

import my.alkarps.engine.helper.notvalid.*;
import my.alkarps.engine.helper.valid.TestClassWithPublicConstructorAndTestMethods;
import my.alkarps.engine.helper.valid.TestClassWithTestAndAfterEachMethods;
import my.alkarps.engine.helper.valid.TestClassWithTestAndBeforeEachAndAfterEachMethods;
import my.alkarps.engine.helper.valid.TestClassWithTestAndBeforeEachMethods;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

class EngineTest {

    @Test
    void run_WhenClassIsNull() {
        assertThatCode(() -> Engine.run(null))
                .isInstanceOf(NotValidClassException.class);
    }

    @Test
    void run_WhenClassEmptyWithPrivateConstructor() {
        assertThatCode(() -> Engine.run(EmptyTestClassWithPrivateConstructor.class))
                .isInstanceOf(NotValidClassException.class);
    }

    @Test
    void run_WhenClassEmptyWithPrivateConstructorWithArgs() {
        assertThatCode(() -> Engine.run(EmptyTestClassWithPrivateConstructorWithArgs.class))
                .isInstanceOf(NotValidClassException.class);
    }

    @Test
    void run_WhenClassEmptyWithPublicConstructorWithArgs() {
        assertThatCode(() -> Engine.run(EmptyTestClassWithPublicConstructorWithArgs.class))
                .isInstanceOf(NotValidClassException.class);
    }

    @Test
    void run_WhenClassEmptyWithPublicConstructor() {
        assertThatCode(() -> Engine.run(EmptyTestClassWithPublicConstructor.class))
                .isInstanceOf(NotValidClassException.class);
    }

    @Test
    void run_WhenClassEmptyWithPublicConstructorButNotWithTestMethods() {
        assertThatCode(() -> Engine.run(TestClassWithPublicConstructorButNotTestMethods.class))
                .isInstanceOf(NotValidClassException.class);
    }

    @Test
    void run_WhenClassWithPublicConstructorAndTestMethods() {
        assertThatCode(() -> Engine.run(TestClassWithPublicConstructorAndTestMethods.class))
                .doesNotThrowAnyException();
    }

    @Test
    void run_WhenClassValidAndHasNotValidBeforeEachMethods() {
        assertThatCode(() -> Engine.run(TestClassWithNotValidBeforeEachMethods.class))
                .isInstanceOf(NotValidClassException.class);
    }

    @Test
    void run_WhenClassHasOnlyBeforeEachMethods() {
        assertThatCode(() -> Engine.run(TestClassWithOnlyBeforeEachMethods.class))
                .isInstanceOf(NotValidClassException.class);
    }

    @Test
    void run_WhenClassValidAndHasNotValidAfterEachMethods() {
        assertThatCode(() -> Engine.run(TestClassWithNotValidAfterEachMethods.class))
                .isInstanceOf(NotValidClassException.class);
    }

    @Test
    void run_WhenClassHasOnlyAfterEachMethods() {
        assertThatCode(() -> Engine.run(TestClassWithOnlyAfterEachMethods.class))
                .isInstanceOf(NotValidClassException.class);
    }

    @Test
    void run_WhenClassHasOnlyBeforeAndAfterEachMethods() {
        assertThatCode(() -> Engine.run(TestClassWithOnlyAfterAndBeforeEachMethods.class))
                .isInstanceOf(NotValidClassException.class);
    }

    @Test
    void run_WhenClassHasTestAndBeforeEachMethods() {
        assertThatCode(() -> Engine.run(TestClassWithTestAndBeforeEachMethods.class))
                .doesNotThrowAnyException();
    }

    @Test
    void run_WhenClassHasTestAndAfterEachMethods() {
        assertThatCode(() -> Engine.run(TestClassWithTestAndAfterEachMethods.class))
                .doesNotThrowAnyException();
    }

    @Test
    void run_WhenClassHasTestAndBeforeEachAndAfterEachMethods() {
        assertThatCode(() -> Engine.run(TestClassWithTestAndBeforeEachAndAfterEachMethods.class))
                .doesNotThrowAnyException();
    }
}
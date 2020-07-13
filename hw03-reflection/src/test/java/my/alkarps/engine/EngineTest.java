package my.alkarps.engine;

import my.alkarps.engine.helper.EmptyTestClassWithPrivateConstructor;
import my.alkarps.engine.helper.EmptyTestClassWithPrivateConstructorWithArgs;
import my.alkarps.engine.helper.EmptyTestClassWithPublicConstructor;
import my.alkarps.engine.helper.EmptyTestClassWithPublicConstructorWithArgs;
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
}
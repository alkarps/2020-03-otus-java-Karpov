package my.alkarps.engine;

import my.alkarps.engine.helper.notvalid.*;
import my.alkarps.engine.helper.valid.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

class EngineTest {

    @Test
    void run_WhenClassIsNull() {
        assertThatCode(() -> Engine.run(null))
                .isInstanceOf(NotValidClassException.class)
                .hasMessage("Класс не валиден: Класс не указан.");
    }

    @Test
    void run_WhenClassEmptyWithPrivateConstructor() {
        assertThatCode(() -> Engine.run(EmptyTestClassWithPrivateConstructor.class))
                .isInstanceOf(NotValidClassException.class)
                .hasMessage("Класс не валиден: Конструктор класса должен быть public и быть без аргументов");
    }

    @Test
    void run_WhenClassEmptyWithPrivateConstructorWithArgs() {
        assertThatCode(() -> Engine.run(EmptyTestClassWithPrivateConstructorWithArgs.class))
                .isInstanceOf(NotValidClassException.class)
                .hasMessage("Класс не валиден: Конструктор класса должен быть public и быть без аргументов");
    }

    @Test
    void run_WhenClassEmptyWithPublicConstructorWithArgs() {
        assertThatCode(() -> Engine.run(EmptyTestClassWithPublicConstructorWithArgs.class))
                .isInstanceOf(NotValidClassException.class)
                .hasMessage("Класс не валиден: Конструктор класса должен быть public и быть без аргументов");
    }

    @Test
    void run_WhenClassEmptyWithPublicConstructor() {
        assertThatCode(() -> Engine.run(EmptyTestClassWithPublicConstructor.class))
                .isInstanceOf(NotValidClassException.class)
                .hasMessage("Класс не валиден: Отсутствуют методы для тестирования.");
    }

    @Test
    void run_WhenClassEmptyWithPublicConstructorButNotWithTestMethods() {
        assertThatCode(() -> Engine.run(TestClassWithPublicConstructorButNotTestMethods.class))
                .isInstanceOf(NotValidClassException.class)
                .hasMessage("Класс не валиден: Отсутствуют методы для тестирования.");
    }

    @Test
    void run_WhenClassValidAndHasNotValidBeforeEachMethods() {
        assertThatCode(() -> Engine.run(TestClassWithNotValidBeforeEachMethods.class))
                .isInstanceOf(NotValidClassException.class)
                .hasMessage("Класс не валиден: Метод, аннотированный BeforeEach, должен быть без модификатора static");
    }

    @Test
    void run_WhenClassHasOnlyBeforeEachMethods() {
        assertThatCode(() -> Engine.run(TestClassWithOnlyBeforeEachMethods.class))
                .isInstanceOf(NotValidClassException.class)
                .hasMessage("Класс не валиден: Отсутствуют методы для тестирования.");
    }

    @Test
    void run_WhenClassValidAndHasNotValidAfterEachMethods() {
        assertThatCode(() -> Engine.run(TestClassWithNotValidAfterEachMethods.class))
                .isInstanceOf(NotValidClassException.class)
                .hasMessage("Класс не валиден: Метод, аннотированный AfterEach, должен быть без модификатора static");
    }

    @Test
    void run_WhenClassHasOnlyAfterEachMethods() {
        assertThatCode(() -> Engine.run(TestClassWithOnlyAfterEachMethods.class))
                .isInstanceOf(NotValidClassException.class)
                .hasMessage("Класс не валиден: Отсутствуют методы для тестирования.");
    }

    @Test
    void run_WhenClassHasOnlyBeforeAndAfterEachMethods() {
        assertThatCode(() -> Engine.run(TestClassWithOnlyAfterAndBeforeEachMethods.class))
                .isInstanceOf(NotValidClassException.class)
                .hasMessage("Класс не валиден: Отсутствуют методы для тестирования.");
    }

    @Test
    void run_WhenClassWithPublicConstructorAndTestMethods() {
        assertThatCode(() -> Engine.run(TestClassWithPublicConstructorAndTestMethods.class))
                .doesNotThrowAnyException();
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

    @Test
    void run_WhenClassHasTestMethodAndFailBeforeEachMethod() {
        assertThatCode(() -> Engine.run(TestClassWithTestMethodAndFailBeforeEachMethod.class))
                .doesNotThrowAnyException();
    }

    @Test
    void run_WhenClassHasFailTestMethod() {
        assertThatCode(() -> Engine.run(TestClassWithFailTestMethod.class))
                .doesNotThrowAnyException();
    }

    @Test
    void run_WhenClassHasTestMethodAndFailAfterEachMethod() {
        assertThatCode(() -> Engine.run(TestClassWithTestMethodAndFailAfterEachMethod.class))
                .doesNotThrowAnyException();
    }
}
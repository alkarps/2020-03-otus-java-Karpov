package my.alkarps.engine;

import my.alkarps.engine.exception.ClassNotFoundException;
import my.alkarps.engine.exception.ClassWithoutTestMethodException;
import my.alkarps.engine.exception.MethodHasStaticModifierException;
import my.alkarps.engine.exception.NotValidConstructorException;
import my.alkarps.engine.helper.notvalid.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * @author alkarps
 * create date 23.07.2020 8:58
 */
class AnalyzerTest {

    private Analyzer analyzer;

    @BeforeEach
    void setUp() {
        analyzer = new Analyzer();
    }

    @Test
    void run_WhenClassIsNull() {
        assertThatCode(() -> analyzer.analyze(null))
                .isInstanceOf(ClassNotFoundException.class)
                .hasMessage("Класс не валиден: Класс не указан.");
    }

    @Test
    void run_WhenClassEmptyWithPrivateConstructor() {
        assertThatCode(() -> analyzer.analyze(EmptyTestClassWithPrivateConstructor.class))
                .isInstanceOf(NotValidConstructorException.class)
                .hasMessage("Класс не валиден: Конструктор класса должен быть public и быть без аргументов");
    }

    @Test
    void run_WhenClassEmptyWithPrivateConstructorWithArgs() {
        assertThatCode(() -> analyzer.analyze(EmptyTestClassWithPrivateConstructorWithArgs.class))
                .isInstanceOf(NotValidConstructorException.class)
                .hasMessage("Класс не валиден: Конструктор класса должен быть public и быть без аргументов");
    }

    @Test
    void run_WhenClassEmptyWithPublicConstructorWithArgs() {
        assertThatCode(() -> analyzer.analyze(EmptyTestClassWithPublicConstructorWithArgs.class))
                .isInstanceOf(NotValidConstructorException.class)
                .hasMessage("Класс не валиден: Конструктор класса должен быть public и быть без аргументов");
    }

    @Test
    void run_WhenClassEmptyWithPublicConstructor() {
        assertThatCode(() -> analyzer.analyze(EmptyTestClassWithPublicConstructor.class))
                .isInstanceOf(ClassWithoutTestMethodException.class)
                .hasMessage("Класс не валиден: Отсутствуют методы для тестирования.");
    }

    @Test
    void run_WhenClassEmptyWithPublicConstructorButNotWithTestMethods() {
        assertThatCode(() -> analyzer.analyze(TestClassWithPublicConstructorButNotTestMethods.class))
                .isInstanceOf(ClassWithoutTestMethodException.class)
                .hasMessage("Класс не валиден: Отсутствуют методы для тестирования.");
    }

    @Test
    void run_WhenClassValidAndHasNotValidBeforeEachMethods() {
        assertThatCode(() -> analyzer.analyze(TestClassWithNotValidBeforeEachMethods.class))
                .isInstanceOf(MethodHasStaticModifierException.class)
                .hasMessage("Класс не валиден: Метод, аннотированный BeforeEach, должен быть без модификатора static");
    }

    @Test
    void run_WhenClassHasOnlyBeforeEachMethods() {
        assertThatCode(() -> analyzer.analyze(TestClassWithOnlyBeforeEachMethods.class))
                .isInstanceOf(ClassWithoutTestMethodException.class)
                .hasMessage("Класс не валиден: Отсутствуют методы для тестирования.");
    }

    @Test
    void run_WhenClassValidAndHasNotValidAfterEachMethods() {
        assertThatCode(() -> analyzer.analyze(TestClassWithNotValidAfterEachMethods.class))
                .isInstanceOf(MethodHasStaticModifierException.class)
                .hasMessage("Класс не валиден: Метод, аннотированный AfterEach, должен быть без модификатора static");
    }

    @Test
    void run_WhenClassHasOnlyAfterEachMethods() {
        assertThatCode(() -> analyzer.analyze(TestClassWithOnlyAfterEachMethods.class))
                .isInstanceOf(ClassWithoutTestMethodException.class)
                .hasMessage("Класс не валиден: Отсутствуют методы для тестирования.");
    }

    @Test
    void run_WhenClassHasOnlyBeforeAndAfterEachMethods() {
        assertThatCode(() -> analyzer.analyze(TestClassWithOnlyAfterAndBeforeEachMethods.class))
                .isInstanceOf(ClassWithoutTestMethodException.class)
                .hasMessage("Класс не валиден: Отсутствуют методы для тестирования.");
    }
}
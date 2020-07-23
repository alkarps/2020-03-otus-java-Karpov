package my.alkarps.engine;

import my.alkarps.engine.exception.ClassNotFoundException;
import my.alkarps.engine.exception.ClassWithoutTestMethodException;
import my.alkarps.engine.exception.MethodHasStaticModifierException;
import my.alkarps.engine.exception.NotValidConstructorException;
import my.alkarps.engine.helper.notvalid.*;
import my.alkarps.engine.helper.valid.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EngineTest {

    private Engine engine;
    private Analyzer analyzer;
    private Runner runner;

    @BeforeEach
    public void setUp() {
        analyzer = spy(new Analyzer());
        runner = spy(new Runner());
        engine = new Engine(analyzer, runner);
    }

    @Test
    void run_WhenClassIsNull() {
        Class<?> testClass = null;
        assertThatCode(() -> engine.run(testClass))
                .isInstanceOf(ClassNotFoundException.class)
                .hasMessage("Класс не валиден: Класс не указан.");
        verifyAnalizer(testClass);
        verify(runner, never()).run(any());
    }

    @Test
    void run_WhenClassEmptyWithPrivateConstructor() {
        Class<?> testClass = EmptyTestClassWithPrivateConstructor.class;
        assertThatCode(() -> engine.run(testClass))
                .isInstanceOf(NotValidConstructorException.class)
                .hasMessage("Класс не валиден: Конструктор класса должен быть public и быть без аргументов");
        verifyAnalizer(testClass);
        verify(runner, never()).run(any());
    }

    @Test
    void run_WhenClassEmptyWithPrivateConstructorWithArgs() {
        Class<?> testClass = EmptyTestClassWithPrivateConstructorWithArgs.class;
        assertThatCode(() -> engine.run(testClass))
                .isInstanceOf(NotValidConstructorException.class)
                .hasMessage("Класс не валиден: Конструктор класса должен быть public и быть без аргументов");
        verifyAnalizer(testClass);
        verify(runner, never()).run(any());
    }

    @Test
    void run_WhenClassEmptyWithPublicConstructorWithArgs() {
        Class<?> testClass = EmptyTestClassWithPublicConstructorWithArgs.class;
        assertThatCode(() -> engine.run(testClass))
                .isInstanceOf(NotValidConstructorException.class)
                .hasMessage("Класс не валиден: Конструктор класса должен быть public и быть без аргументов");
        verifyAnalizer(testClass);
        verify(runner, never()).run(any());
    }

    @Test
    void run_WhenClassEmptyWithPublicConstructor() {
        Class<?> testClass = EmptyTestClassWithPublicConstructor.class;
        assertThatCode(() -> engine.run(testClass))
                .isInstanceOf(ClassWithoutTestMethodException.class)
                .hasMessage("Класс не валиден: Отсутствуют методы для тестирования.");
        verifyAnalizer(testClass);
        verify(runner, never()).run(any());
    }

    @Test
    void run_WhenClassEmptyWithPublicConstructorButNotWithTestMethods() {
        Class<?> testClass = TestClassWithPublicConstructorButNotTestMethods.class;
        assertThatCode(() -> engine.run(testClass))
                .isInstanceOf(ClassWithoutTestMethodException.class)
                .hasMessage("Класс не валиден: Отсутствуют методы для тестирования.");
        verifyAnalizer(testClass);
        verify(runner, never()).run(any());
    }

    @Test
    void run_WhenClassValidAndHasNotValidBeforeEachMethods() {
        Class<?> testClass = TestClassWithNotValidBeforeEachMethods.class;
        assertThatCode(() -> engine.run(testClass))
                .isInstanceOf(MethodHasStaticModifierException.class)
                .hasMessage("Класс не валиден: Метод, аннотированный BeforeEach, должен быть без модификатора static");
        verifyAnalizer(testClass);
        verify(runner, never()).run(any());
    }

    @Test
    void run_WhenClassHasOnlyBeforeEachMethods() {
        Class<?> testClass = TestClassWithOnlyBeforeEachMethods.class;
        assertThatCode(() -> engine.run(testClass))
                .isInstanceOf(ClassWithoutTestMethodException.class)
                .hasMessage("Класс не валиден: Отсутствуют методы для тестирования.");
        verifyAnalizer(testClass);
        verify(runner, never()).run(any());
    }

    @Test
    void run_WhenClassValidAndHasNotValidAfterEachMethods() {
        Class<?> testClass = TestClassWithNotValidAfterEachMethods.class;
        assertThatCode(() -> engine.run(testClass))
                .isInstanceOf(MethodHasStaticModifierException.class)
                .hasMessage("Класс не валиден: Метод, аннотированный AfterEach, должен быть без модификатора static");
        verifyAnalizer(testClass);
        verify(runner, never()).run(any());
    }

    @Test
    void run_WhenClassHasOnlyAfterEachMethods() {
        Class<?> testClass = TestClassWithOnlyAfterEachMethods.class;
        assertThatCode(() -> engine.run(testClass))
                .isInstanceOf(ClassWithoutTestMethodException.class)
                .hasMessage("Класс не валиден: Отсутствуют методы для тестирования.");
        verifyAnalizer(testClass);
        verify(runner, never()).run(any());
    }

    @Test
    void run_WhenClassHasOnlyBeforeAndAfterEachMethods() {
        Class<?> testClass = TestClassWithOnlyAfterAndBeforeEachMethods.class;
        assertThatCode(() -> engine.run(testClass))
                .isInstanceOf(ClassWithoutTestMethodException.class)
                .hasMessage("Класс не валиден: Отсутствуют методы для тестирования.");
        verifyAnalizer(testClass);
        verify(runner, never()).run(any());
    }

    @Test
    void run_WhenClassConstructorFail() {
        Class<?> testClass = TestClassWithFailConstructor.class;
        testWithoutAnyExceptions(testClass);
        verifyAnalizer(testClass);
    }

    @Test
    void run_WhenClassWithPublicConstructorAndTestMethods() {
        Class<?> testClass = TestClassWithPublicConstructorAndTestMethods.class;
        testWithoutAnyExceptions(testClass);
        verifyAnalizer(testClass);
    }

    @Test
    void run_WhenClassHasTestAndBeforeEachMethods() {
        Class<?> testClass = TestClassWithTestAndBeforeEachMethods.class;
        testWithoutAnyExceptions(testClass);
        verifyAnalizer(testClass);
    }

    @Test
    void run_WhenClassHasTestAndAfterEachMethods() {
        Class<?> testClass = TestClassWithTestAndAfterEachMethods.class;
        testWithoutAnyExceptions(testClass);
        verifyAnalizer(testClass);
    }

    @Test
    void run_WhenClassHasTestAndBeforeEachAndAfterEachMethods() {
        Class<?> testClass = TestClassWithTestAndBeforeEachAndAfterEachMethods.class;
        testWithoutAnyExceptions(testClass);
        verifyAnalizer(testClass);
    }

    @Test
    void run_WhenClassHasTestMethodAndFailBeforeEachMethod() {
        Class<?> testClass = TestClassWithTestMethodAndFailBeforeEachMethod.class;
        testWithoutAnyExceptions(testClass);
        verifyAnalizer(testClass);
    }

    @Test
    void run_WhenClassHasFailTestMethod() {
        Class<?> testClass = TestClassWithFailTestMethod.class;
        testWithoutAnyExceptions(testClass);
        verifyAnalizer(testClass);
    }

    @Test
    void run_WhenClassHasTestMethodAndFailAfterEachMethod() {
        Class<?> testClass = TestClassWithTestMethodAndFailAfterEachMethod.class;
        testWithoutAnyExceptions(testClass);
        verifyAnalizer(testClass);
    }

    private void testWithoutAnyExceptions(Class<?> testClass) {
        assertThatCode(() -> engine.run(testClass))
                .doesNotThrowAnyException();
    }

    private void verifyAnalizer(Class<?> testClass) {
        verify(analyzer).analyze(testClass);
        verify(analyzer).analyze(testClass);
    }
}
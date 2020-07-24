package my.alkarps.engine;

import my.alkarps.engine.exception.execute.CreateTestInstanceClassException;
import my.alkarps.engine.exception.execute.InvokeSupportMethodException;
import my.alkarps.engine.exception.validate.ClassNotFoundException;
import my.alkarps.engine.exception.validate.*;
import my.alkarps.engine.helper.ResultCaptor;
import my.alkarps.engine.helper.fail.*;
import my.alkarps.engine.helper.inheritance.without.*;
import my.alkarps.engine.helper.invalid.*;
import my.alkarps.engine.model.ClassDetails;
import my.alkarps.engine.model.Statistics;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

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

    @ParameterizedTest
    @MethodSource("notValidConstructor")
    void run_WhenClassEmptyWithPrivateConstructor(Class<?> testClass) {
        assertThatCode(() -> engine.run(testClass))
                .isInstanceOf(NotValidConstructorException.class)
                .hasMessage("Класс не валиден: Конструктор класса должен быть public и быть без аргументов");
        verifyAnalizer(testClass);
        verify(runner, never()).run(any());
    }

    static Stream<Class<?>> notValidConstructor() {
        return Stream.of(PublicConstructorWithArgs.class,
                PrivateConstructorWithArgs.class,
                PrivateConstructor.class);
    }

    @Test
    void run_WhenClassValidAndHasNotValidBeforeEachMethods() {
        Class<?> testClass = NotValidBeforeEachMethods.class;
        assertThatCode(() -> engine.run(testClass))
                .isInstanceOf(MethodHasStaticModifierException.class)
                .hasMessage("Класс не валиден: Метод, аннотированный BeforeEach, должен быть без модификатора static");
        verifyAnalizer(testClass);
        verify(runner, never()).run(any());
    }

    @Test
    void run_WhenClassValidAndHasNotValidAfterEachMethods() {
        Class<?> testClass = NotValidAfterEachMethods.class;
        assertThatCode(() -> engine.run(testClass))
                .isInstanceOf(MethodHasStaticModifierException.class)
                .hasMessage("Класс не валиден: Метод, аннотированный AfterEach, должен быть без модификатора static");
        verifyAnalizer(testClass);
        verify(runner, never()).run(any());
    }

    @Test
    void run_WhenClassValidAndHasNotValidBeforeAllMethods() {
        Class<?> testClass = NotValidBeforeAllMethods.class;
        assertThatCode(() -> engine.run(testClass))
                .isInstanceOf(MethodNotHasStaticModifierException.class)
                .hasMessage("Класс не валиден: Метод, аннотированный BeforeAll, должен быть только с модификатором static");
        verifyAnalizer(testClass);
        verify(runner, never()).run(any());
    }

    @Test
    void run_WhenClassValidAndHasNotValidAfterAllMethods() {
        Class<?> testClass = NotValidAfterAllMethods.class;
        assertThatCode(() -> engine.run(testClass))
                .isInstanceOf(MethodNotHasStaticModifierException.class)
                .hasMessage("Класс не валиден: Метод, аннотированный AfterAll, должен быть только с модификатором static");
        verifyAnalizer(testClass);
        verify(runner, never()).run(any());
    }

    @ParameterizedTest
    @MethodSource("withoutMethods")
    void run_WhenClassWithoutTestMethods() {
        Class<?> testClass = WithoutAnyMethods.class;
        assertThatCode(() -> engine.run(testClass))
                .isInstanceOf(ClassWithoutTestMethodException.class)
                .hasMessage("Класс не валиден: Отсутствуют методы для тестирования.");
        verifyAnalizer(testClass);
        verify(runner, never()).run(any());
    }

    static Stream<Class<?>> withoutMethods() {
        return Stream.of(WithoutAnyMethods.class,
                WithoutTestMethods.class,
                OnlyBeforeEachMethods.class,
                OnlyAfterEachMethods.class,
                OnlyAfterAndBeforeEachMethods.class,
                OnlyAfterAndBeforeAllMethods.class,
                OnlyAfterAllMethods.class,
                OnlyBeforeAllMethods.class);
    }

    @Test
    void run_WhenClassConstructorFail() {
        Class<?> testClass = FailConstructor.class;
        assertThatCode(() -> engine.run(testClass))
                .isInstanceOf(CreateTestInstanceClassException.class)
                .hasMessage("Ошибка инициализации тестового класса");
        verifyAnalizer(testClass);
    }

    @ParameterizedTest
    @MethodSource("supportMethodsFail")
    void run_WhenSupportMethodIsFail(Class<?> testClass) {
        assertThatCode(() -> engine.run(testClass))
                .isInstanceOf(InvokeSupportMethodException.class)
                .hasMessage("Ошибка при выполнении статических вспомогательных методов");
        verifyAnalizer(testClass);
    }

    static Stream<Class<?>> supportMethodsFail() {
        return Stream.of(FailWithAfterAllMethod.class, FailWithBeforeAllMethod.class);
    }

    @Test
    void run_WhenClassValid_thenRunTest() {
        Class<?> testClass = OnlyTestMethods.class;
        ResultCaptor<ClassDetails> classDetailsCaptor = new ResultCaptor<>();
        doAnswer(classDetailsCaptor).when(analyzer).analyze(testClass);
        ResultCaptor<Statistics> statisticsCaptor = new ResultCaptor<>();
        doAnswer(statisticsCaptor).when(runner).run(any());
        testWithoutAnyExceptions(testClass);
        Assertions.assertNotNull(classDetailsCaptor.getResult());
        Assertions.assertNotNull(statisticsCaptor.getResult());
        verifyAnalizer(testClass);
    }

    @ParameterizedTest
    @MethodSource("validTest")
    void run_WhenClassValid_thenRunTest(Class<?> testClass) {
        testWithoutAnyExceptions(testClass);
        verifyAnalizer(testClass);
    }

    static Stream<Class<?>> validTest() {
        return Stream.of(TestAndBeforeEachMethods.class,
                TestAndAfterEachMethods.class,
                TestAndBeforeEachAndAfterEachMethods.class,
                WorkTestMethodButFailBeforeEachMethod.class,
                FailTestMethod.class,
                WorkTestButFailAfterEachMethod.class,
                TestAndAfterAllMethods.class,
                TestAndAfterEachAndAfterAllMethods.class,
                TestAndBeforeAllAndAfterAllMethods.class,
                TestAndBeforeAllAndAfterEachAndAfterAllMethods.class,
                TestAndBeforeAllAndAfterEachMethods.class,
                TestAndBeforeAllAndBeforeEachAndAfterAllMethods.class,
                TestAndBeforeAllAndBeforeEachAndAfterEachAndAfterAllMethods.class,
                TestAndBeforeAllAndBeforeEachAndAfterEachMethods.class,
                TestAndBeforeAllAndBeforeEachMethods.class,
                TestAndBeforeAllMethods.class,
                TestAndBeforeEachAndAfterAllMethods.class,
                TestAndBeforeEachAndAfterEachAndAfterAllMethods.class,
                TestAndBeforeEachAndAfterEachMethods.class,
                TestAndBeforeEachMethods.class);
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
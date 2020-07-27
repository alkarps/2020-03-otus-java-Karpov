package my.alkarps.engine;

import my.alkarps.engine.exception.execute.InvokeSupportMethodException;
import my.alkarps.engine.exception.validate.ClassNotFoundException;
import my.alkarps.engine.exception.validate.*;
import my.alkarps.engine.helper.StatisticsCaptor;
import my.alkarps.engine.helper.fail.*;
import my.alkarps.engine.helper.inheritance.with.OnlyTestMethodWithInheritance;
import my.alkarps.engine.helper.inheritance.without.*;
import my.alkarps.engine.helper.invalid.*;
import my.alkarps.engine.model.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
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

    @ParameterizedTest
    @MethodSource("validTest")
    void run_WhenClassValid_thenRunTest(Class<?> testClass, Statistics statistics) {
        StatisticsCaptor statisticsCaptor = new StatisticsCaptor();
        doAnswer(statisticsCaptor).when(runner).run(any());
        testWithoutAnyExceptions(testClass);
        verifyAnalizer(testClass);
        verify(runner).run(any());
        assertThat(statisticsCaptor.getResult()).isEqualToComparingFieldByField(statistics);
    }

    static Stream<Arguments> validTest() {
        return Stream.of(createArguments(OnlyTestMethods.class),
                createArguments(TestAndBeforeEachMethods.class),
                createArguments(TestAndAfterEachMethods.class),
                createArguments(TestAndBeforeEachAndAfterEachMethods.class),
                createArgumentsForFailTest(WorkTestMethodButFailBeforeEachMethod.class),
                createArgumentsForFailTest(FailConstructor.class),
                createArgumentsForFailTest(FailTestMethod.class),
                createArgumentsForFailTest(WorkTestButFailAfterEachMethod.class),
                createArguments(TestAndAfterAllMethods.class),
                createArguments(TestAndAfterEachAndAfterAllMethods.class),
                createArguments(TestAndBeforeAllAndAfterAllMethods.class),
                createArguments(TestAndBeforeAllAndAfterEachAndAfterAllMethods.class),
                createArguments(TestAndBeforeAllAndAfterEachMethods.class),
                createArguments(TestAndBeforeAllAndBeforeEachAndAfterAllMethods.class),
                createArguments(TestAndBeforeAllAndBeforeEachAndAfterEachAndAfterAllMethods.class),
                createArguments(TestAndBeforeAllAndBeforeEachAndAfterEachMethods.class),
                createArguments(TestAndBeforeAllAndBeforeEachMethods.class),
                createArguments(TestAndBeforeAllMethods.class),
                createArguments(TestAndBeforeEachAndAfterAllMethods.class),
                createArguments(TestAndBeforeEachAndAfterEachAndAfterAllMethods.class),
                createArguments(TestAndBeforeEachAndAfterEachMethods.class),
                createArguments(TestAndBeforeEachMethods.class),
                createArguments(OnlyTestMethodWithInheritance.class));
    }

    private static Arguments createArguments(Class<?> testClass) {
        return Arguments.of(testClass, createStatistics(testClass));
    }

    private static Statistics createStatistics(Class<?> testClass) {
        return new Statistics(testClass.getSimpleName(), 3, 3);
    }

    private static Arguments createArgumentsForFailTest(Class<?> testClass) {
        return Arguments.of(testClass, createStatisticsForFailTest(testClass));
    }

    private static Statistics createStatisticsForFailTest(Class<?> testClass) {
        return new Statistics(testClass.getSimpleName(), 1, 0);
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
package my.alkarps.engine;

import my.alkarps.engine.exception.NotValidClassException;
import my.alkarps.engine.model.ClassDetails;
import my.alkarps.engine.model.Statistics;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class Engine {

    private final Analyzer analyzer;

    public Engine() {
        this.analyzer = new Analyzer();
    }

    public Engine(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    public void run(Class<?> testClass) {
        ClassDetails classDetails = analyzer.analyze(testClass);
        Statistics statistics = executeTest(classDetails);
        System.out.println(statistics);
    }

    private Statistics executeTest(ClassDetails classDetails) {
        Statistics.StatisticsBuilder statistics = Statistics.builder()
                .classTestName(classDetails.getClassName());
        Instant startClassTest = Instant.now();
        Object instance = newInstance(classDetails);
//        invokeSupportMethods(instance, classDetails.getBeforeAllMethods());
        for (ClassDetails.MethodDetails method : classDetails.getTestMethods()) {
            statistics.testMethodResult(testMethod(instance, method));
        }
//        invokeSupportMethods(instance, classDetails.getAfterAllMethods());
        return statistics
                .classTestTime(Duration.between(startClassTest, Instant.now()))
                .build();
    }

    private Statistics.TestMethodResult testMethod(Object instance, ClassDetails.MethodDetails methodDetails) {
        Statistics.TestMethodResult.TestMethodResultBuilder methodResult = Statistics.TestMethodResult.builder()
                .methodName(methodDetails.getMethod().getName());
        Instant startTest = Instant.now();
        try {
            invokeSupportMethods(instance, methodDetails.getBeforeEachMethods());
            methodDetails.getMethod().setAccessible(true);
            methodDetails.getMethod().invoke(instance);
            invokeSupportMethods(instance, methodDetails.getAfterEachMethods());
            methodResult.methodTestTime(Duration.between(startTest, Instant.now()))
                    .success(true);
        } catch (Exception ex) {
            methodResult.methodTestTime(Duration.between(startTest, Instant.now()))
                    .success(false)
                    .throwable(ex);
        }
        return methodResult.build();
    }

    private void invokeSupportMethods(Object instance, List<Method> suppertMethods) throws IllegalAccessException, InvocationTargetException {
        for (Method method : suppertMethods) {
            method.setAccessible(true);
            method.invoke(instance);
        }
    }

    private Object newInstance(ClassDetails classDetails) {
        try {
            return classDetails.getConstructor().newInstance();
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new NotValidClassException(e);
        }
    }
}

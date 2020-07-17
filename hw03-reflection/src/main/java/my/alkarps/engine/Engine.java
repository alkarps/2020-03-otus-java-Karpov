package my.alkarps.engine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class Engine {
    public static void run(Class<?> testClass) {
        ClassDetails classDetails = Analyzer.analyze(testClass);
        Statistics statistics = executeTest(classDetails);
    }

    private static Statistics executeTest(ClassDetails classDetails) {
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

    private static Statistics.TestMethodResult testMethod(Object instance, ClassDetails.MethodDetails methodDetails) {
        System.out.println(methodDetails.getMethod().getName());
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
            System.err.println(ex);
            methodResult.methodTestTime(Duration.between(startTest, Instant.now()))
                    .success(false)
                    .throwable(ex);
        }
        return methodResult.build();
    }

    private static void invokeSupportMethods(Object instance, List<Method> suppertMethods) throws IllegalAccessException, InvocationTargetException {
        for (Method method : suppertMethods) {
            method.setAccessible(true);
            method.invoke(instance);
        }
    }

    private static Object newInstance(ClassDetails classDetails) {
        try {
            return classDetails.getConstructor().newInstance();
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new NotValidClassException(e);
        }
    }
}

package my.alkarps.engine;

import my.alkarps.engine.exception.execute.CreateTestInstanceClassException;
import my.alkarps.engine.exception.execute.InvokeSupportMethodException;
import my.alkarps.engine.model.ClassDetails;
import my.alkarps.engine.model.Statistics;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * @author alkarps
 * create date 23.07.2020 9:25
 */
public class Runner {
    Statistics run(ClassDetails classDetails) {
        Statistics.StatisticsBuilder statistics = Statistics.builder()
                .classTestName(classDetails.getClassName());
        Instant startClassTest = Instant.now();
        Object instance = null;
        try {
            instance = newInstance(classDetails);
            invokeSupportMethods(instance, classDetails.getBeforeAllMethods());
            for (ClassDetails.MethodDetails method : classDetails.getTestMethods()) {
                statistics.testMethodResult(testMethod(instance, method));
            }
            invokeSupportMethods(instance, classDetails.getAfterAllMethods());
            return statistics
                    .classTestTime(Duration.between(startClassTest, Instant.now()))
                    .build();
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            if (instance != null) {
                throw new InvokeSupportMethodException(e);
            }
            throw new CreateTestInstanceClassException(e);
        }
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

    private Object newInstance(ClassDetails classDetails) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return classDetails.getConstructor().newInstance();
    }
}

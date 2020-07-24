package my.alkarps.engine;

import my.alkarps.engine.exception.execute.CreateTestInstanceClassException;
import my.alkarps.engine.exception.execute.InvokeSupportMethodException;
import my.alkarps.engine.model.ClassDetails;
import my.alkarps.engine.model.Statistics;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author alkarps
 * create date 23.07.2020 9:25
 */
public class Runner {
    Statistics run(ClassDetails classDetails) {
        Statistics statistics = new Statistics(classDetails.getClassName(), classDetails.getTestMethods().size());
        Object instance = null;
        try {
            instance = newInstance(classDetails);
            invokeSupportMethods(instance, classDetails.getBeforeAllMethods());
            for (ClassDetails.MethodDetails method : classDetails.getTestMethods()) {
                boolean result = testMethod(instance, method);
                if (result) {
                    statistics.addSuccess();
                }
            }
            invokeSupportMethods(instance, classDetails.getAfterAllMethods());
            return statistics;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            if (instance != null) {
                throw new InvokeSupportMethodException(e);
            }
            throw new CreateTestInstanceClassException(e);
        }
    }

    private boolean testMethod(Object instance, ClassDetails.MethodDetails methodDetails) {
        boolean success = true;
        try {
            invokeSupportMethods(instance, methodDetails.getBeforeEachMethods());
            methodDetails.getMethod().setAccessible(true);
            methodDetails.getMethod().invoke(instance);
            invokeSupportMethods(instance, methodDetails.getAfterEachMethods());
        } catch (Exception ex) {
            success = false;
            ex.printStackTrace();
        }
        System.out.println(methodDetails.getMethod().getName() + (success ? ": УСПЕХ" : ": ОШИБКА"));
        return success;
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

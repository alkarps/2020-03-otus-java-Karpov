package my.alkarps.engine;

import my.alkarps.engine.exception.execute.InvokeSupportMethodException;
import my.alkarps.engine.model.ClassDetails;
import my.alkarps.engine.model.Statistics;

import java.lang.reflect.Constructor;
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
        try {
            invokeStaticSupportMethods(classDetails.getBeforeAllMethods());
            for (ClassDetails.MethodDetails method : classDetails.getTestMethods()) {
                boolean result = testMethod(classDetails.getConstructor(), method);
                if (result) {
                    statistics.addSuccess();
                }
            }
            invokeStaticSupportMethods(classDetails.getAfterAllMethods());
            return statistics;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new InvokeSupportMethodException(e);
        }
    }

    private boolean testMethod(Constructor<?> instanceConstructor, ClassDetails.MethodDetails methodDetails) {
        boolean success = true;
        try {
            Object instance = instanceConstructor.newInstance();
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

    private void invokeStaticSupportMethods(List<Method> supportMethods) throws IllegalAccessException, InvocationTargetException {
        invokeSupportMethods(null, supportMethods);
    }

    private void invokeSupportMethods(Object instance, List<Method> supportMethods) throws IllegalAccessException, InvocationTargetException {
        for (Method method : supportMethods) {
            method.setAccessible(true);
            method.invoke(instance);
        }
    }
}

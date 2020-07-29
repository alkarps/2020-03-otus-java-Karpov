package my.alkarps.engine.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author alkarps
 * create date 13.07.2020 10:57
 */
@Builder
@Getter
@RequiredArgsConstructor
public class ClassDetails {
    private final String className;
    private final Constructor<?> constructor;
    private final List<Method> beforeAllMethods;
    private final List<MethodDetails> testMethods;
    private final List<Method> afterAllMethods;

    @Builder
    @Getter
    @RequiredArgsConstructor
    public static class MethodDetails {
        private final List<Method> beforeEachMethods;
        private final Method method;
        private final List<Method> afterEachMethods;
    }
}

package my.alkarps.engine;

import my.alkarps.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * @author alkarps
 * create date 13.07.2020 10:57
 */
public class Analyzer {

    public static ClassDetails analyze(String testClass) {
        try {
            throwExceptionIfNotValid(isNullOrEmpty(testClass), "Класс не указан.");
            return analyze(Class.forName(testClass));
        } catch (ClassNotFoundException e) {
            throw new NotValidClassException(e);
        }
    }

    public static ClassDetails analyze(Class<?> testClass) {
        throwExceptionIfNotValid(testClass == null, "Класс не указан.");
        ClassDetails classDetails = ClassDetails.builder()
                .className(testClass.getCanonicalName())
                .constructor(findConstructor(testClass))
                .beforeAllMethods(findMethodsWithAnnotation(testClass, BeforeAll.class))
                .testMethods(findTestMethods(testClass))
                .afterAllMethods(findMethodsWithAnnotation(testClass, AfterAll.class))
                .build();
        throwExceptionIfNotValid(classDetails.getTestMethods().isEmpty(), "Отсутствуют методы для тестирования.");
        return classDetails;
    }

    private static void throwExceptionIfNotValid(boolean condition, String message) {
        if (condition) {
            throw new NotValidClassException(message);
        }
    }

    private static Constructor<?> findConstructor(Class<?> testClass) {
        return Arrays.stream(testClass.getConstructors())
                .filter(defCons -> Modifier.isPublic(defCons.getModifiers()))
                .filter(defCons -> !defCons.isVarArgs())
                .findFirst().orElseThrow(() -> new NotValidClassException("Конструктор класса должен быть public и быть без аргументов"));
    }

    private static List<Method> findMethodsWithAnnotation(Class<?> testClass, Class<? extends Annotation> annotation) {
        return Stream.of(testClass.getDeclaredMethods())
                .filter(method -> !Modifier.isPrivate(method.getModifiers()))
                .filter(method -> method.isAnnotationPresent(annotation))
                .collect(Collectors.toList());
    }

    private static List<ClassDetails.MethodDetails> findTestMethods(Class<?> testClass) {
        List<Method> beforeEach = findMethodsWithAnnotation(testClass, BeforeEach.class);
        List<Method> afterEach = findMethodsWithAnnotation(testClass, AfterEach.class);
        return findMethodsWithAnnotation(testClass, Test.class).stream()
                .map(method -> ClassDetails.MethodDetails.builder()
                        .beforeEachMethods(beforeEach)
                        .method(method)
                        .afterEachMethods(afterEach)
                        .build())
                .collect(Collectors.toList());
    }
}

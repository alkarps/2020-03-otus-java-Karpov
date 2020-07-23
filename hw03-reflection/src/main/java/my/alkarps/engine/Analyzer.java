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

/**
 * @author alkarps
 * create date 13.07.2020 10:57
 */
public class Analyzer {

    public static ClassDetails analyze(Class<?> testClass) {
        throwExceptionIfNotValid(testClass == null, "Класс не указан.");
        ClassDetails classDetails = ClassDetails.builder()
                .className(testClass.getCanonicalName())
                .constructor(findConstructor(testClass))
                .beforeAllMethods(findMethodsWithAnnotation(testClass, BeforeAll.class)
                        .collect(Collectors.toList()))
                .testMethods(findTestMethods(testClass))
                .afterAllMethods(findMethodsWithAnnotation(testClass, AfterAll.class)
                        .collect(Collectors.toList()))
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

    private static Stream<Method> findMethodsWithAnnotation(Class<?> testClass, Class<? extends Annotation> annotation) {
        return Stream.of(testClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotation));
    }

    private static List<ClassDetails.MethodDetails> findTestMethods(Class<?> testClass) {
        List<Method> beforeEach = findMethodsWithAnnotation(testClass, BeforeEach.class).collect(Collectors.toList());
        throwExceptionIfNotValid(hasStaticMethod(beforeEach), "Метод, аннотированный BeforeEach, должен быть без модификатора static");
        List<Method> afterEach = findMethodsWithAnnotation(testClass, AfterEach.class).collect(Collectors.toList());
        throwExceptionIfNotValid(hasStaticMethod(afterEach), "Метод, аннотированный AfterEach, должен быть без модификатора static");
        return findMethodsWithAnnotation(testClass, Test.class)
                .filter(method -> !Modifier.isPrivate(method.getModifiers()))
                .map(method -> ClassDetails.MethodDetails.builder()
                        .beforeEachMethods(beforeEach)
                        .method(method)
                        .afterEachMethods(afterEach)
                        .build())
                .collect(Collectors.toList());
    }

    private static boolean hasStaticMethod(List<Method> methods) {
        return methods.stream().anyMatch(method -> Modifier.isStatic(method.getModifiers()));
    }
}

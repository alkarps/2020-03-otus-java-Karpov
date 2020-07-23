package my.alkarps.engine;

import my.alkarps.annotation.*;
import my.alkarps.engine.exception.ClassNotFoundException;
import my.alkarps.engine.exception.ClassWithoutTestMethodException;
import my.alkarps.engine.exception.MethodHasStaticModifierException;
import my.alkarps.engine.exception.NotValidConstructorException;
import my.alkarps.engine.model.ClassDetails;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author alkarps
 * create date 13.07.2020 10:57
 */
public class Analyzer {

    public ClassDetails analyze(Class<?> testClass) {
        throwExceptionIfNotValid(testClass == null, ClassNotFoundException::new);
        return ClassDetails.builder()
                .className(testClass.getCanonicalName())
                .constructor(findConstructor(testClass))
                .beforeAllMethods(findMethodsWithAnnotation(testClass, BeforeAll.class))
                .testMethods(findTestMethods(testClass))
                .afterAllMethods(findMethodsWithAnnotation(testClass, AfterAll.class))
                .build();
    }

    private Constructor<?> findConstructor(Class<?> testClass) {
        return Arrays.stream(testClass.getConstructors())
                .filter(defCons -> Modifier.isPublic(defCons.getModifiers()))
                .filter(defCons -> !defCons.isVarArgs())
                .findFirst()
                .orElseThrow(NotValidConstructorException::new);
    }

    private Stream<Method> findMethodsWithAnnotationStream(Class<?> testClass, Class<? extends Annotation> annotation) {
        return Stream.of(testClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotation));
    }

    private List<Method> findMethodsWithAnnotation(Class<?> testClass, Class<? extends Annotation> annotation) {
        return findMethodsWithAnnotationStream(testClass, annotation)
                .collect(Collectors.toList());
    }

    private List<Method> findAndNonStaticValidationMethodsWithAnnotation(Class<?> testClass, Class<? extends Annotation> annotation) {
        List<Method> methods = findMethodsWithAnnotation(testClass, annotation);
        boolean hasStatic = hasStaticMethod(methods);
        throwExceptionIfNotValid(hasStatic, () -> new MethodHasStaticModifierException(annotation));
        return methods;
    }

    private List<ClassDetails.MethodDetails> findTestMethods(Class<?> testClass) {
        List<Method> beforeEach = findAndNonStaticValidationMethodsWithAnnotation(testClass, BeforeEach.class);
        List<Method> afterEach = findAndNonStaticValidationMethodsWithAnnotation(testClass, AfterEach.class);
        List<ClassDetails.MethodDetails> methods = findMethodsWithAnnotationStream(testClass, Test.class)
                .filter(method -> !Modifier.isPrivate(method.getModifiers()))
                .map(method -> ClassDetails.MethodDetails.builder()
                        .beforeEachMethods(beforeEach)
                        .method(method)
                        .afterEachMethods(afterEach)
                        .build())
                .collect(Collectors.toList());
        throwExceptionIfNotValid(methods.isEmpty(), ClassWithoutTestMethodException::new);
        return methods;
    }

    private boolean hasStaticMethod(List<Method> methods) {
        return methods.stream().anyMatch(method -> Modifier.isStatic(method.getModifiers()));
    }

    private <X extends Throwable> void throwExceptionIfNotValid(boolean condition, Supplier<? extends X> exceptionSupplier) throws X {
        if (condition) {
            throw exceptionSupplier.get();
        }
    }
}

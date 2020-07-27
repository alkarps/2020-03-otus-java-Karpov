package my.alkarps.engine;

import my.alkarps.annotation.*;
import my.alkarps.engine.exception.validate.ClassNotFoundException;
import my.alkarps.engine.exception.validate.*;
import my.alkarps.engine.model.ClassDetails;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
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
                .className(testClass.getSimpleName())
                .constructor(findConstructor(testClass))
                .beforeAllMethods(findAndStaticValidationMethodsWithAnnotation(testClass, BeforeAll.class))
                .afterAllMethods(findAndStaticValidationMethodsWithAnnotation(testClass, AfterAll.class))
                .testMethods(findTestMethods(testClass))
                .build();
    }

    private Constructor<?> findConstructor(Class<?> testClass) {
        return Arrays.stream(testClass.getConstructors())
                .filter(defCons -> Modifier.isPublic(defCons.getModifiers()))
                .filter(defCons -> !defCons.isVarArgs())
                .findFirst()
                .orElseThrow(NotValidConstructorException::new);
    }

    private Stream<Method> getDeclaredMethods(Class<?> testClass) {
        if (testClass == Object.class) {
            return Stream.empty();
        }
        return Stream.concat(Stream.of(testClass.getDeclaredMethods()), getDeclaredMethods(testClass.getSuperclass()));
    }

    private Stream<Method> findMethodsWithAnnotationStream(Class<?> testClass, Class<? extends Annotation> annotation) {
        return getDeclaredMethods(testClass)
                .filter(method -> method.isAnnotationPresent(annotation));
    }

    private List<Method> findMethodsWithAnnotation(Class<?> testClass, Class<? extends Annotation> annotation) {
        return findMethodsWithAnnotationStream(testClass, annotation)
                .collect(Collectors.toList());
    }

    private List<Method> findAndValidateMethodsWithAnnotation(Class<?> testClass, Class<? extends Annotation> annotation, Consumer<List<Method>> consumer) {
        List<Method> methods = findMethodsWithAnnotation(testClass, annotation);
        if (!methods.isEmpty()) {
            consumer.accept(methods);
        }
        return methods;
    }

    private List<Method> findAndNonStaticValidationMethodsWithAnnotation(Class<?> testClass, Class<? extends Annotation> annotation) {
        return findAndValidateMethodsWithAnnotation(testClass, annotation, methods -> {
            boolean hasStatic = hasStaticMethod(methods);
            throwExceptionIfNotValid(hasStatic, () -> new MethodHasStaticModifierException(annotation));
        });
    }

    private List<Method> findAndStaticValidationMethodsWithAnnotation(Class<?> testClass, Class<? extends Annotation> annotation) {
        return findAndValidateMethodsWithAnnotation(testClass, annotation, methods -> {
            boolean hasAllStatic = hasAllStaticMethod(methods);
            throwExceptionIfNotValid(!hasAllStatic, () -> new MethodNotHasStaticModifierException(annotation));
        });
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

    private boolean hasAllStaticMethod(List<Method> methods) {
        return methods.stream().allMatch(method -> Modifier.isStatic(method.getModifiers()));
    }

    private <X extends Throwable> void throwExceptionIfNotValid(boolean condition, Supplier<? extends X> exceptionSupplier) throws X {
        if (condition) {
            throw exceptionSupplier.get();
        }
    }
}

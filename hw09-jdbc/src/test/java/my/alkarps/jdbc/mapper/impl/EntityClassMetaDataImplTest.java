package my.alkarps.jdbc.mapper.impl;

import my.alkarps.jdbc.mapper.annotation.Id;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * @author alkarps
 * create date 06.08.2020 10:21
 */
class EntityClassMetaDataImplTest {

    @Test
    void initEntity_whenClassIsNull_thenThrowIllegalArgumentException() {
        assertThatCode(() -> new EntityClassMetaDataImpl<>(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Class not initialized");
    }

    @ParameterizedTest
    @MethodSource("classesWithWrongConstructor")
    void initEntity_whenClassHasPrivateConstructor_thenThrowIllegalArgumentException(Class<?> testClass) {
        assertThatCode(() -> new EntityClassMetaDataImpl<>(testClass))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Constructor didn't find");
    }

    private static Stream<Class<?>> classesWithWrongConstructor() {
        return Stream.of(ClassWithPrivateConstructor.class, ClassWithConstructorWithArguments.class);
    }

    private static class ClassWithPrivateConstructor {
        private ClassWithPrivateConstructor() {
        }
    }

    private static class ClassWithConstructorWithArguments {
        public ClassWithConstructorWithArguments(Object arg, Object... otherArgs) {
        }
    }

    @ParameterizedTest
    @MethodSource("classesWithoutId")
    void initEntity_whenClassNotHaveFieldWithAnnotationId_thenThrowNoSuchElementException(Class<?> testClass) {
        assertThatCode(() -> new EntityClassMetaDataImpl<>(testClass))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No value present");
    }

    private static Stream<Class<?>> classesWithoutId() {
        return Stream.of(EmptyClass.class, ClassWithoutId.class);
    }

    public static class EmptyClass {
    }

    public static class ClassWithoutId {
        private long id;
    }

    @ParameterizedTest
    @MethodSource("classesWithId")
    void initEntity_whenClassHaveFieldWithAnnotationId_thenReturnThis(Class<?> testClass, int fieldCount) throws NoSuchFieldException {
        assertThat(new EntityClassMetaDataImpl<>(testClass))
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("name", testClass.getSimpleName())
                .hasFieldOrPropertyWithValue("idField", testClass.getDeclaredField("id"))
                .extracting(EntityClassMetaDataImpl::getAllFields)
                .asList()
                .hasSize(fieldCount);
    }

    public static class ClassWithOnlyId {
        @Id
        private long id;
    }

    public static class ClassWithId {
        @Id
        private long id;
        private String name;
    }

    private static Stream<Arguments> classesWithId() {
        return Stream.of(
                Arguments.of(ClassWithOnlyId.class, 1),
                Arguments.of(ClassWithId.class, 2)
        );
    }
}
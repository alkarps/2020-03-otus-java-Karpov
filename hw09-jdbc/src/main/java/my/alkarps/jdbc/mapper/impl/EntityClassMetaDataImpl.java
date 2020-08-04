package my.alkarps.jdbc.mapper.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import my.alkarps.jdbc.mapper.EntityClassMetaData;
import my.alkarps.jdbc.mapper.annotation.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Stream.empty;

/**
 * @author alkarps
 * create date 03.08.2020 22:33
 */
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    String name;
    Constructor<T> constructor;
    Field idField;
    List<Field> allFields;
    List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> tClass) {
        throwExceptionIfNull(tClass);
        this.name = tClass.getName();
        this.constructor = findDefaultConstructor(tClass);
        this.allFields = findAllField(tClass);
        this.fieldsWithoutId = allFields.stream()
                .filter(field -> !haveIdAnnotation(field))
                .collect(Collectors.toList());
        this.idField = allFields.stream()
                .filter(this::haveIdAnnotation)
                .findAny()
                .orElseThrow();
    }

    boolean haveIdAnnotation(Field field) {
        return field.getDeclaredAnnotation(Id.class) != null;
    }

    void throwExceptionIfNull(Class<T> tClass) {
        if (tClass == null) {
            throw new IllegalArgumentException("Class not initialized");
        }
    }

    Constructor<T> findDefaultConstructor(Class<T> tClass) {
        return (Constructor<T>) Stream.of(tClass.getConstructors())
                .filter(constructor -> !constructor.isVarArgs())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Constructor didn't find"));
    }

    List<Field> findAllField(Class<T> tClass) {
        return findAllFieldStream(tClass).collect(Collectors.toList());
    }

    Stream<Field> findAllFieldStream(Class<?> tClass) {
        if (tClass.getSuperclass().equals(Object.class)) {
            return empty();
        }
        return Stream.concat(Stream.of(tClass.getDeclaredFields()), findAllFieldStream(tClass.getSuperclass()));
    }
}

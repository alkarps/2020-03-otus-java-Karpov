package my.alkarps.jdbc.mapper.impl;

import my.alkarps.core.dao.UserDaoException;
import my.alkarps.jdbc.dao.UserDaoJdbcExecutor;
import my.alkarps.jdbc.mapper.EntityClassMetaData;
import my.alkarps.jdbc.mapper.ObjectConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author alkarps
 * create date 04.08.2020 7:55
 */
public class ObjectConverterImpl<T> implements ObjectConverter<T> {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoJdbcExecutor.class);

    @Override
    public List<Object> extractParamsForInsert(T object, EntityClassMetaData<T> metaData) {
        return extractParams(metaData.getAllFields(), object).collect(Collectors.toList());
    }

    @Override
    public List<Object> extractParamsForUpdate(T object, EntityClassMetaData<T> metaData) {
        return Stream.of(Stream.concat(
                extractParams(metaData.getFieldsWithoutId(), object),
                Stream.of(extractValueFromField(metaData.getIdField(), object))
        )).collect(Collectors.toList());
    }

    @Override
    public T fillObjectForSelect(ResultSet rs, EntityClassMetaData<T> metaData) {
        try {
            T object = metaData.getConstructor().newInstance();
            for (Field field : metaData.getAllFields()) {
                field.set(object, rs.getObject(field.getName()));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    private Stream<Object> extractParams(List<Field> fields, T object) {
        return fields.stream()
                .map(field -> extractValueFromField(field, object));
    }

    private Object extractValueFromField(Field field, T object) {
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new UserDaoException(e);
        }
    }
}

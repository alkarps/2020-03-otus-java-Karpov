package my.alkarps.jdbc.mapper;

import java.sql.ResultSet;
import java.util.List;

/**
 * @author alkarps
 * create date 04.08.2020 7:50
 */
public interface ObjectConverter<T> {
    List<Object> extractParamsForInsert(T object, EntityClassMetaData<T> metaData);

    List<Object> extractParamsForUpdate(T object, EntityClassMetaData<T> metaData);

    T fillObjectForSelect(ResultSet rs, EntityClassMetaData<T> metaData);
}

package my.alkarps.jdbc.mapper.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import my.alkarps.jdbc.mapper.EntityClassMetaData;
import my.alkarps.jdbc.mapper.EntitySQLMetaData;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author alkarps
 * create date 03.08.2020 23:00
 */
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    String selectAllSql;
    String selectByIdSql;
    String insertSql;
    String updateSql;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> metaData) {
        String params = formParamsList(metaData.getAllFields());
        this.selectAllSql = String.format("select %s from %s",
                params,
                metaData.getName());
        this.selectByIdSql = String.format("select %s from %s where %s = ?",
                params,
                metaData.getName(),
                metaData.getIdField().getName());
        this.insertSql = String.format("insert into %s (%s) value (%s)",
                metaData.getName(),
                params,
                formInputPlaceHolder(metaData.getAllFields()));
        this.updateSql = String.format("update %s set %s where %s = ?",
                metaData.getName(),
                formUpdateParamsList(metaData.getFieldsWithoutId()),
                metaData.getIdField().getName());
    }

    private String formInputPlaceHolder(List<Field> fields) {
        return fields.stream()
                .map(f -> "?")
                .collect(Collectors.joining(","));
    }

    private String formParamsList(List<Field> fields) {
        return formParamsWithSuffix(fields, "");
    }

    private String formUpdateParamsList(List<Field> fields) {
        return formParamsWithSuffix(fields, " = ?");
    }

    private String formParamsWithSuffix(List<Field> fields, String suffix) {
        return fields.stream()
                .map(field -> field.getName() + suffix)
                .collect(Collectors.joining(","));
    }
}

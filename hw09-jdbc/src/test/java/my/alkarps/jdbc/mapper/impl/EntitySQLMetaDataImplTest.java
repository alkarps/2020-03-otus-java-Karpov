package my.alkarps.jdbc.mapper.impl;

import my.alkarps.jdbc.mapper.EntityClassMetaData;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * @author alkarps
 * create date 07.08.2020 10:24
 */
class EntitySQLMetaDataImplTest {
    private static final String INSERT = "insertSql";
    private static final String UPDATE = "updateSql";
    private static final String SELECT = "selectAllSql";
    private static final String SELECT_BY_ID = "selectByIdSql";

    @ParameterizedTest
    @MethodSource("testData")
    void init(EntityClassMetaData<?> metaData, Map<String, String> sql) {
        assertThat(new EntitySQLMetaDataImpl(metaData))
                .isNotNull()
                .hasFieldOrPropertyWithValue(INSERT, sql.get(INSERT))
                .hasFieldOrPropertyWithValue(UPDATE, sql.get(UPDATE))
                .hasFieldOrPropertyWithValue(SELECT, sql.get(SELECT))
                .hasFieldOrPropertyWithValue(SELECT_BY_ID, sql.get(SELECT_BY_ID));
    }

    private static Stream<Arguments> testData() {
        return Stream.of(
                createArguments("test", "id", "field"),
                createArguments("user", "id", "name", "age"),
                createArguments("account", "no")
        );
    }

    private static Arguments createArguments(String tableName, String idName, String... fields) {
        return Arguments.of(createAndSetupClassMetaDataMock(tableName, idName, fields),
                generateSqlByParams(tableName, idName, fields));
    }

    private static Map<String, String> generateSqlByParams(String tableName, String idName, String... fields) {
        Map<String, String> sql = new HashMap<>();
        String params = Stream.concat(Stream.of(fields), Stream.of(idName))
                .collect(Collectors.joining(","));
        sql.put(SELECT, String.format("select %s from %s",
                params,
                tableName));
        sql.put(SELECT_BY_ID, String.format("select %s from %s where %s = ?",
                params,
                tableName,
                idName));
        sql.put(INSERT, String.format("insert into %s (%s) values (%s)",
                tableName,
                params,
                Stream.concat(Stream.of(idName), Stream.of(fields))
                        .map(s -> "?")
                        .collect(Collectors.joining(","))));
        sql.put(UPDATE, String.format("update %s set %s where %s = ?",
                tableName,
                Stream.of(fields)
                        .map(s -> s + " = ?")
                        .collect(Collectors.joining(",")),
                idName));
        return sql;
    }

    private static EntityClassMetaData<?> createAndSetupClassMetaDataMock(String tableName, String idName, String... fields) {
        Field id = createAndSetupField(idName);
        List<Field> fieldsWithoutId = Stream.of(fields)
                .map(EntitySQLMetaDataImplTest::createAndSetupField)
                .collect(Collectors.toList());
        List<Field> allFields = new ArrayList<>(fieldsWithoutId);
        allFields.add(id);
        EntityClassMetaData<?> metaData = mock(EntityClassMetaData.class);
        doReturn(id).when(metaData).getIdField();
        doReturn(fieldsWithoutId).when(metaData).getFieldsWithoutId();
        doReturn(allFields).when(metaData).getAllFields();
        doReturn(tableName).when(metaData).getName();
        return metaData;
    }

    private static Field createAndSetupField(String name) {
        Field f = mock(Field.class);
        doReturn(name).when(f).getName();
        return f;
    }
}
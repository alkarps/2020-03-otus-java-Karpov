package my.alkarps.jdbc.mapper.impl;

import my.alkarps.core.dao.UserDaoException;
import my.alkarps.jdbc.executor.DbExecutor;
import my.alkarps.jdbc.mapper.EntityClassMetaData;
import my.alkarps.jdbc.mapper.EntitySQLMetaData;
import my.alkarps.jdbc.mapper.JdbcMapper;
import my.alkarps.jdbc.mapper.ObjectConverter;
import my.alkarps.jdbc.sessionmanager.SessionManagerJdbc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * @author alkarps
 * create date 01.08.2020 18:10
 */
public class JdbcMapperEager<T> implements JdbcMapper<T> {
    private static final Logger logger = LoggerFactory.getLogger(JdbcMapperEager.class);

    private final SessionManagerJdbc sessionManager;
    private final DbExecutor<T> dbExecutor;
    private final ObjectConverter<T> objectConverter;
    private final EntityClassMetaData<T> metaData;
    private final EntitySQLMetaData sqlMetaData;

    public JdbcMapperEager(SessionManagerJdbc sessionManager,
                           DbExecutor<T> dbExecutor,
                           ObjectConverter<T> objectConverter,
                           Class<T> tClass) {
        this.sessionManager = sessionManager;
        this.dbExecutor = dbExecutor;
        this.objectConverter = objectConverter;
        this.metaData = new EntityClassMetaDataImpl<>(tClass);
        this.sqlMetaData = new EntitySQLMetaDataImpl(metaData);
    }

    @Override
    public long insert(T objectData) {
        if (objectData == null) {
            throw new IllegalArgumentException("Object is null");
        }
        try {
            return dbExecutor.executeInsert(getConnection(),
                    sqlMetaData.getInsertSql(),
                    objectConverter.extractParamsForInsert(objectData, metaData));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public void update(T objectData) {
        if (objectData == null) {
            throw new IllegalArgumentException("Object is null");
        }
        try {
            dbExecutor.executeUpdate(getConnection(),
                    sqlMetaData.getUpdateSql(),
                    objectConverter.extractParamsForUpdate(objectData, metaData));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public long insertOrUpdate(T objectData) {
        if (objectData == null) {
            throw new IllegalArgumentException("Object is null");
        }
        try {
            long affectedRow = dbExecutor.executeUpdate(getConnection(),
                    sqlMetaData.getUpdateSql(),
                    objectConverter.extractParamsForUpdate(objectData, metaData));
            if (affectedRow < 1) {
                return dbExecutor.executeInsert(getConnection(),
                        sqlMetaData.getInsertSql(),
                        objectConverter.extractParamsForInsert(objectData, metaData));
            }
            return -1;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public Optional<T> findById(long id, Class<T> clazz) {
        try {
            return dbExecutor.executeSelect(getConnection(),
                    sqlMetaData.getSelectByIdSql(),
                    id,
                    this::selectOneObjectFromSelect);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    private T selectOneObjectFromSelect(ResultSet rs) {
        try {
            if (rs.next()) {
                return objectConverter.fillObjectForSelect(rs, metaData);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    private Connection getConnection() {
        return sessionManager.getCurrentSession().getConnection();
    }
}

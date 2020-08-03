package my.alkarps.jdbc.mapper.impl;

import my.alkarps.core.dao.UserDaoException;
import my.alkarps.jdbc.dao.UserDaoJdbcExecutor;
import my.alkarps.jdbc.executor.DbExecutor;
import my.alkarps.jdbc.mapper.EntityClassMetaData;
import my.alkarps.jdbc.mapper.EntitySQLMetaData;
import my.alkarps.jdbc.mapper.JdbcMapper;
import my.alkarps.jdbc.sessionmanager.SessionManagerJdbc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Collections;
import java.util.Optional;

/**
 * @author alkarps
 * create date 01.08.2020 18:10
 */
public class JdbcMapperEager<T> implements JdbcMapper<T> {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoJdbcExecutor.class);

    private final SessionManagerJdbc sessionManager;
    private final DbExecutor<T> dbExecutor;
    private final EntityClassMetaData<T> metaData;
    private final EntitySQLMetaData sqlMetaData;

    public JdbcMapperEager(SessionManagerJdbc sessionManager,
                           DbExecutor<T> dbExecutor,
                           Class<T> tClass) {
        this.sessionManager = sessionManager;
        this.dbExecutor = dbExecutor;
        this.metaData = new EntityClassMetaDataImpl<>(tClass);
        this.sqlMetaData = new EntitySQLMetaDataImpl(metaData);
    }

    @Override
    public long insert(T objectData) {
        try {
            return dbExecutor.executeInsert(getConnection(), sqlMetaData.getInsertSql(),
                    Collections.singletonList(user.getName()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public void update(T objectData) {
        try {
            dbExecutor.executeUpdate(getConnection(), sqlMetaData.getUpdateSql(), Collections.singletonList(objectData));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public long insertOrUpdate(T objectData) {
        try {
            long affectedRow = dbExecutor.executeUpdate(getConnection(), sqlMetaData.getUpdateSql(), Collections.singletonList(objectData));
            if (affectedRow < 1) {
                return dbExecutor.executeInsert(getConnection(), sqlMetaData.getInsertSql(),
                        Collections.singletonList(user.getName()));
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
            return dbExecutor.executeSelect(getConnection(), sqlMetaData.getSelectByIdSql(),
                    id, rs -> {
                        try {
                            if (rs.next()) {
                                T object = metaData.getConstructor().newInstance();
                                for (Field f : metaData.getAllFields()) {
                                    f.set(object, rs.getObject(f.getName()));
                                }
                                return object;
                            }
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }
                        return null;
                    });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    private Connection getConnection() {
        return sessionManager.getCurrentSession().getConnection();
    }
}

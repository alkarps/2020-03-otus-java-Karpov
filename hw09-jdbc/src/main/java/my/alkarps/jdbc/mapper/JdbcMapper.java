package my.alkarps.jdbc.mapper;

import java.util.Optional;

public interface JdbcMapper<T> {
    long insert(T objectData);

    void update(T objectData);

    long insertOrUpdate(T objectData);

    Optional<T> findById(long id, Class<T> clazz);
}

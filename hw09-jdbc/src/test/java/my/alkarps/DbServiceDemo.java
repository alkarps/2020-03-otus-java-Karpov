package my.alkarps;

import my.alkarps.core.model.User;
import my.alkarps.core.service.DBServiceUser;
import my.alkarps.core.service.DbServiceUserImpl;
import my.alkarps.h2.DataSourceH2;
import my.alkarps.jdbc.dao.UserDaoJdbcExecutor;
import my.alkarps.jdbc.executor.DbExecutor;
import my.alkarps.jdbc.executor.DbExecutorImpl;
import my.alkarps.jdbc.mapper.JdbcMapper;
import my.alkarps.jdbc.mapper.ObjectConverter;
import my.alkarps.jdbc.mapper.impl.JdbcMapperEager;
import my.alkarps.jdbc.mapper.impl.ObjectConverterImpl;
import my.alkarps.jdbc.sessionmanager.SessionManagerJdbc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author alkarps
 * create date 31.07.2020 14:00
 */
public class DbServiceDemo {
    private DBServiceUser dbServiceUser;

    @BeforeEach
    public void setUp() {
        var dataSource = new DataSourceH2();
        var sessionManager = new SessionManagerJdbc(dataSource);
        DbExecutor<User> dbExecutor = new DbExecutorImpl<>();
        ObjectConverter<User> objectConverter = new ObjectConverterImpl<User>();
        JdbcMapper<User> jdbcMapper = new JdbcMapperEager<>(sessionManager, dbExecutor, objectConverter, User.class);
        var userDao = new UserDaoJdbcExecutor(sessionManager, jdbcMapper);
        dbServiceUser = new DbServiceUserImpl(userDao);
    }

    @Test
    void demo() {
        User newUser = createUser(0, "dbServiceUser", 10);
        var expectedId = 1L;
        assertThat(dbServiceUser.saveUser(newUser)).isEqualTo(expectedId);
        assertThat(dbServiceUser.getUser(expectedId))
                .isPresent().get()
                .hasFieldOrPropertyWithValue("name", newUser.getName())
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("age", newUser.getAge());
    }

    private User createUser(long id, String name, int age) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setAge(age);
        return user;
    }
}

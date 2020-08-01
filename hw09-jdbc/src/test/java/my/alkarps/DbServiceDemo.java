package my.alkarps;

import my.alkarps.core.model.User;
import my.alkarps.core.service.DBServiceUser;
import my.alkarps.core.service.DbServiceUserImpl;
import my.alkarps.h2.DataSourceH2;
import my.alkarps.jdbc.dao.UserDaoJdbcExecutor;
import my.alkarps.jdbc.executor.DbExecutor;
import my.alkarps.jdbc.executor.DbExecutorImpl;
import my.alkarps.jdbc.mapper.JdbcMapper;
import my.alkarps.jdbc.mapper.JdbcMapperImpl;
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
        JdbcMapper<User> jdbcMapper = new JdbcMapperImpl<>(sessionManager, dbExecutor);
        var userDao = new UserDaoJdbcExecutor(sessionManager, jdbcMapper);
        dbServiceUser = new DbServiceUserImpl(userDao);
    }

    @Test
    void demo() {
        User newUser = new User(0, "dbServiceUser");
        var expectedId = 1L;
        assertThat(dbServiceUser.saveUser(newUser)).isEqualTo(expectedId);
        assertThat(dbServiceUser.getUser(expectedId))
                .isPresent().get()
                .hasFieldOrPropertyWithValue("name", newUser.getName());
    }
}

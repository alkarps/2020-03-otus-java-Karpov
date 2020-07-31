package my.alkarps;

import my.alkarps.core.model.User;
import my.alkarps.core.service.DBServiceUser;
import my.alkarps.core.service.DbServiceUserImpl;
import my.alkarps.h2.DataSourceH2;
import my.alkarps.jdbc.DbExecutorImpl;
import my.alkarps.jdbc.dao.UserDaoJdbc;
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
        DbExecutorImpl<User> dbExecutor = new DbExecutorImpl<>();
        var userDao = new UserDaoJdbc(sessionManager, dbExecutor);
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

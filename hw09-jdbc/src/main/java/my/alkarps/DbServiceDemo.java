package my.alkarps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import my.alkarps.core.model.User;
import my.alkarps.core.service.DbServiceUserImpl;
import my.alkarps.h2.DataSourceH2;
import my.alkarps.jdbc.DbExecutorImpl;
import my.alkarps.jdbc.dao.UserDaoJdbc;
import my.alkarps.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Optional;

/**
 * @author sergey
 * created on 03.02.19.
 */
public class DbServiceDemo {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceDemo.class);

    public static void main(String[] args) throws Exception {
        var dataSource = new DataSourceH2();

        var sessionManager = new SessionManagerJdbc(dataSource);
        DbExecutorImpl<User> dbExecutor = new DbExecutorImpl<>();
        var userDao = new UserDaoJdbc(sessionManager, dbExecutor);

        var dbServiceUser = new DbServiceUserImpl(userDao);
        var id = dbServiceUser.saveUser(new User(0, "dbServiceUser"));
        Optional<User> user = dbServiceUser.getUser(id);

        user.ifPresentOrElse(
                crUser -> logger.info("created user, name:{}", crUser.getName()),
                () -> logger.info("user was not created")
        );

    }
}

package my.alkarps;

import my.alkarps.core.model.Account;
import my.alkarps.core.model.User;
import my.alkarps.core.service.DBServiceAccount;
import my.alkarps.core.service.DBServiceAccountImpl;
import my.alkarps.core.service.DbServiceUserImpl;
import my.alkarps.h2.DataSourceH2;
import my.alkarps.jdbc.dao.AccountDaoJdbcMapper;
import my.alkarps.jdbc.dao.UserDaoJdbcMapper;
import my.alkarps.jdbc.executor.DbExecutor;
import my.alkarps.jdbc.executor.DbExecutorImpl;
import my.alkarps.jdbc.mapper.JdbcMapper;
import my.alkarps.jdbc.mapper.ObjectConverter;
import my.alkarps.jdbc.mapper.impl.JdbcMapperEager;
import my.alkarps.jdbc.mapper.impl.ObjectConverterImpl;
import my.alkarps.jdbc.sessionmanager.SessionManagerJdbc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author alkarps
 * create date 07.08.2020 7:44
 */
public class DbServiceAccountDemo {
    private DBServiceAccount dbServiceAccount;

    @BeforeEach
    public void setUp() {
        var dataSource = new DataSourceH2();
        var sessionManager = new SessionManagerJdbc(dataSource);
        DbExecutor<Account> dbExecutor = new DbExecutorImpl<>();
        ObjectConverter<Account> objectConverter = new ObjectConverterImpl<>();
        JdbcMapper<Account> jdbcMapper = new JdbcMapperEager<>(sessionManager, dbExecutor, objectConverter, Account.class);
        var accountDao = new AccountDaoJdbcMapper(sessionManager, jdbcMapper);
        dbServiceAccount = new DBServiceAccountImpl(accountDao);
    }

//    @Test
    void demo() {
        Account account = createAccount(1L, "dbServiceUser", BigDecimal.TEN);
        var expectedId = 1L;
        assertThat(dbServiceAccount.saveAccount(account)).isEqualTo(expectedId);
        assertThat(dbServiceAccount.getAccount(expectedId))
                .isPresent().get()
                .isEqualToComparingFieldByField(account);
    }

    private Account createAccount(long no, String type, BigDecimal rest) {
        Account account = new Account();
        account.setNo(no);
        account.setType(type);
        account.setRest(rest);
        return account;
    }
}

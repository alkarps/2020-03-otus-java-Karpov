package my.alkarps.jdbc.dao;

import my.alkarps.core.dao.AccountDao;
import my.alkarps.core.dao.UserDaoException;
import my.alkarps.core.model.Account;
import my.alkarps.core.sessionmanager.SessionManager;
import my.alkarps.jdbc.mapper.JdbcMapper;
import my.alkarps.jdbc.sessionmanager.SessionManagerJdbc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class AccountDaoJdbcMapper implements AccountDao {
    private static final Logger logger = LoggerFactory.getLogger(AccountDaoJdbcMapper.class);

    private final SessionManagerJdbc sessionManager;
    private final JdbcMapper<Account> jdbcMapper;

    public AccountDaoJdbcMapper(SessionManagerJdbc sessionManager, JdbcMapper<Account> jdbcMapper) {
        this.sessionManager = sessionManager;
        this.jdbcMapper = jdbcMapper;
    }

    @Override
    public Optional<Account> findById(long id) {
        try {
            return jdbcMapper.findById(id, Account.class);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public long insertAccount(Account account) {
        try {
            return jdbcMapper.insert(account);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public void updateAccount(Account account) {
        jdbcMapper.update(account);
    }

    @Override
    public long insertOrUpdate(Account account) {
        return jdbcMapper.insertOrUpdate(account);
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}

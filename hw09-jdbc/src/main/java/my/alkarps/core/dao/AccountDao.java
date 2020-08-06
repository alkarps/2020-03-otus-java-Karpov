package my.alkarps.core.dao;

import my.alkarps.core.model.Account;
import my.alkarps.core.sessionmanager.SessionManager;

import java.util.Optional;

/**
 * @author alkarps
 * create date 07.08.2020 7:37
 */
public interface AccountDao {
    Optional<Account> findById(long id);

    long insertAccount(Account account);

    void updateAccount(Account account);

    long insertOrUpdate(Account account);

    SessionManager getSessionManager();
}

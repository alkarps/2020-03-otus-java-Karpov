package my.alkarps.core.dao;

import my.alkarps.core.model.User;
import my.alkarps.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface UserDao {
    Optional<User> findById(long id);

    long insertUser(User user);

    void updateUser(User user);

    long insertOrUpdate(User user);

    SessionManager getSessionManager();
}

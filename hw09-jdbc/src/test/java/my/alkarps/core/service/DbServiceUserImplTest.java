package my.alkarps.core.service;

import my.alkarps.core.dao.UserDao;
import my.alkarps.core.model.User;
import my.alkarps.core.sessionmanager.SessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

/**
 * @author alkarps
 * create date 01.08.2020 16:12
 */
class DbServiceUserImplTest {

    private DBServiceUser dbServiceUser;
    private UserDao userDao;
    private SessionManager sessionManager;

    @BeforeEach
    void setUp() {
        sessionManager = mock(SessionManager.class);
        userDao = mock(UserDao.class);
        doReturn(sessionManager).when(userDao).getSessionManager();
        dbServiceUser = new DbServiceUserImpl(userDao);
    }

    @Test
    void saveUser_whenAllOk_thenReturnId() {
        long id = 1;
        User user = createTestUser(0L);

        doNothing().when(sessionManager).beginSession();
        doNothing().when(sessionManager).commitSession();
        doNothing().when(sessionManager).close();
        doReturn(id).when(userDao).insertUser(any());

        assertThat(dbServiceUser.saveUser(user)).isEqualTo(id);

        verify(userDao).getSessionManager();
        verify(sessionManager).beginSession();
        verify(sessionManager).commitSession();
        verify(sessionManager).close();
        verify(userDao).insertUser(user);

        verify(userDao, never()).findById(anyLong());
        verify(userDao, never()).insertOrUpdate(any());
        verify(userDao, never()).updateUser(any());
        verify(sessionManager, never()).getCurrentSession();
        verify(sessionManager, never()).rollbackSession();
    }

    private User createTestUser(long id) {
        User user = new User();
        user.setId(id);
        user.setName("test");
        return user;
    }

    @Test
    void saveUser_whenCatchExceptionWhileInsert_thenThrowDbServiceExceptionAndRollback() {
        User user = createTestUser(0L);

        doNothing().when(sessionManager).beginSession();
        doNothing().when(sessionManager).commitSession();
        doNothing().when(sessionManager).close();
        doThrow(new NullPointerException()).when(userDao).insertUser(any());

        assertThatCode(() -> dbServiceUser.saveUser(user)).isInstanceOf(DbServiceException.class)
                .hasCauseInstanceOf(NullPointerException.class);

        verify(userDao).getSessionManager();
        verify(sessionManager).beginSession();
        verify(sessionManager).rollbackSession();
        verify(sessionManager).close();
        verify(userDao).insertUser(user);

        verify(userDao, never()).findById(anyLong());
        verify(userDao, never()).insertOrUpdate(any());
        verify(userDao, never()).updateUser(any());
        verify(sessionManager, never()).getCurrentSession();
        verify(sessionManager, never()).commitSession();
    }

    @Test
    void saveUser_whenDaoReturnNullSessionManager_thenThrowDbServiceException() {
        User user = createTestUser(0L);

        doReturn(null).when(userDao).getSessionManager();

        assertThatCode(() -> dbServiceUser.saveUser(user)).isInstanceOf(DbServiceException.class)
                .hasCauseInstanceOf(NullPointerException.class);

        verify(userDao).getSessionManager();

        verify(sessionManager, never()).beginSession();
        verify(sessionManager, never()).rollbackSession();
        verify(sessionManager, never()).close();
        verify(userDao, never()).insertUser(any());
        verify(userDao, never()).findById(anyLong());
        verify(userDao, never()).insertOrUpdate(any());
        verify(userDao, never()).updateUser(any());
        verify(sessionManager, never()).getCurrentSession();
        verify(sessionManager, never()).commitSession();
    }

    @Test
    void getUser_whenFoundUserById_thenReturnUser() {
        long id = 1;
        User user = createTestUser(id);

        doNothing().when(sessionManager).beginSession();
        doNothing().when(sessionManager).commitSession();
        doNothing().when(sessionManager).close();
        doReturn(Optional.of(user)).when(userDao).findById(anyLong());

        assertThat(dbServiceUser.getUser(id)).isPresent().get().isEqualTo(user);

        verify(userDao).getSessionManager();
        verify(sessionManager).beginSession();
        verify(sessionManager).close();
        verify(userDao).findById(id);

        verify(userDao, never()).insertUser(any());
        verify(userDao, never()).insertOrUpdate(any());
        verify(userDao, never()).updateUser(any());
        verify(sessionManager, never()).commitSession();
        verify(sessionManager, never()).getCurrentSession();
        verify(sessionManager, never()).rollbackSession();
    }

    @Test
    void getUser_whenNotFindUserById_thenReturnEmptyOptional() {
        long id = 1;

        doNothing().when(sessionManager).beginSession();
        doNothing().when(sessionManager).commitSession();
        doNothing().when(sessionManager).close();
        doReturn(Optional.empty()).when(userDao).findById(anyLong());

        assertThat(dbServiceUser.getUser(id)).isNotPresent();

        verify(userDao).getSessionManager();
        verify(sessionManager).beginSession();
        verify(sessionManager).close();
        verify(userDao).findById(id);

        verify(userDao, never()).insertUser(any());
        verify(userDao, never()).insertOrUpdate(any());
        verify(userDao, never()).updateUser(any());
        verify(sessionManager, never()).commitSession();
        verify(sessionManager, never()).getCurrentSession();
        verify(sessionManager, never()).rollbackSession();
    }

    @Test
    void getUser_whenCatchExceptionWhileFindingUserById_thenReturnEmptyOptional() {
        long id = 1;

        doNothing().when(sessionManager).beginSession();
        doNothing().when(sessionManager).commitSession();
        doNothing().when(sessionManager).close();
        doThrow(new NullPointerException()).when(userDao).findById(anyLong());

        assertThat(dbServiceUser.getUser(id)).isNotPresent();

        verify(userDao).getSessionManager();
        verify(sessionManager).beginSession();
        verify(sessionManager).rollbackSession();
        verify(sessionManager).close();
        verify(userDao).findById(id);

        verify(userDao, never()).insertUser(any());
        verify(userDao, never()).insertOrUpdate(any());
        verify(userDao, never()).updateUser(any());
        verify(sessionManager, never()).commitSession();
        verify(sessionManager, never()).getCurrentSession();
    }

    @Test
    void getUser_whenDaoReturnNullSessionManager_thenThrowDbServiceException() {
        long id = 1;
        doReturn(null).when(userDao).getSessionManager();
        assertThatCode(() -> dbServiceUser.getUser(id)).isInstanceOf(DbServiceException.class)
                .hasCauseInstanceOf(NullPointerException.class);

        verify(userDao).getSessionManager();

        verify(sessionManager, never()).beginSession();
        verify(sessionManager, never()).rollbackSession();
        verify(sessionManager, never()).close();
        verify(userDao, never()).insertUser(any());
        verify(userDao, never()).findById(anyLong());
        verify(userDao, never()).insertOrUpdate(any());
        verify(userDao, never()).updateUser(any());
        verify(sessionManager, never()).getCurrentSession();
        verify(sessionManager, never()).commitSession();
    }
}
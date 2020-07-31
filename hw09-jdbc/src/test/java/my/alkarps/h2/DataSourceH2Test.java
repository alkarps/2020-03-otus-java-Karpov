package my.alkarps.h2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author alkarps
 * create date 31.07.2020 13:43
 */
class DataSourceH2Test {

    private DataSource dataSource;

    @BeforeEach
    public void setUp() {
        dataSource = spy(new DataSourceH2());
    }

    @Test
    void getConnection_whenCall_thenReturnConnection() throws SQLException {
        assertThat(dataSource.getConnection()).isNotNull()
                .isInstanceOf(Connection.class);
    }

    @Test
    void getConnectionWithParams_thenCallGetConnectionWithoutParams() throws SQLException {
        String username = "username";
        String password = "password";
        Connection connection = mock(Connection.class);
        doReturn(connection).when(dataSource).getConnection();
        assertThat(dataSource.getConnection(username, password)).isEqualTo(connection);
        verify(dataSource).getConnection();
    }

    @Test
    void getLogWriter() {
        assertThatCode(() -> dataSource.getLogWriter())
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void setLogWriter() {
        PrintWriter pw = mock(PrintWriter.class);
        assertThatCode(() -> dataSource.setLogWriter(pw))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void getLoginTimeout() {
        assertThatCode(() -> dataSource.getLoginTimeout())
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void setLoginTimeout() {
        assertThatCode(() -> dataSource.setLoginTimeout(10))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void getParentLogger() {
        assertThatCode(() -> dataSource.getParentLogger())
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void unwrap() {
        assertThatCode(() -> dataSource.unwrap(this.getClass()))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void isWrapperFor() {
        assertThatCode(() -> dataSource.isWrapperFor(this.getClass()))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
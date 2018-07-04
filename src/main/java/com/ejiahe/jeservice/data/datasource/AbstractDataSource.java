package com.ejiahe.jeservice.data.datasource;

import com.ejiahe.jeservice.util.CloseEnable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Abstract base class for Spring's {@link DataSource}
 * implementations, taking care of the padding.
 *
 * <p>'Padding' in the context of this class means default implementations
 * for certain methods from the {@code DataSource} interface, such as
 * {@link #getLoginTimeout()}, {@link #setLoginTimeout(int)}, and so forth.
 *
 * @author Juergen Hoeller
 * @since 07.05.2003
 */
public abstract class AbstractDataSource implements DataSource, CloseEnable {

    /** Logger available to subclasses */
    protected final Log logger = LogFactory.getLog(getClass());

    private static PrintWriter DefaultWriter = new PrintWriter(System.err);

    /**
     * Returns 0, indicating the default system timeout is to be used.
     */
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    /**
     * Setting a login timeout is not supported.
     */
    public void setLoginTimeout(int timeout) throws SQLException {
        throw new UnsupportedOperationException("setLoginTimeout");
    }

    /**
     * LogWriter methods are not supported.
     */
    public PrintWriter getLogWriter() {
        return DefaultWriter;
    }

    /**
     * LogWriter methods are not supported.
     */
    public void setLogWriter(PrintWriter pw) throws SQLException {
        throw new UnsupportedOperationException("setLogWriter");
    }


    //---------------------------------------------------------------------
    // Implementation of JDBC 4.0's Wrapper interface
    //---------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface.isInstance(this)) {
            return (T) this;
        }
        throw new SQLException("DataSource of type [" + getClass().getName() +
                "] cannot be unwrapped as [" + iface.getName() + "]");
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isInstance(this);
    }


    //---------------------------------------------------------------------
    // Implementation of JDBC 4.1's getParentLogger method
    //---------------------------------------------------------------------

    public Logger getParentLogger() {
        return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

}
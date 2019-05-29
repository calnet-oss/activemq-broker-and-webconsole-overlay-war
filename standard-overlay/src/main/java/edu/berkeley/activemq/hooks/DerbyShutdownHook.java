package edu.berkeley.activemq.hooks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

// @formatter:off
/**
 * Add this as a shutdown hook.  In activemq.xml:
 *
 * <pre>{@code
 * <shutdownHooks>
 *     <bean xmlns="http://www.springframework.org/schema/beans" class="edu.berkeley.activemq.hooks.DerbyShutdownHook" />
 *     <!-- Other shutdown hooks, such as SpringContextHook -->
 * </shutdownHooks>
 * }</pre>
 */
// @formatter:on
public class DerbyShutdownHook implements Runnable {

    private Logger log = LoggerFactory.getLogger(DerbyShutdownHook.class);

    public void run() {
        log.info("Shutting down Derby");

        // Obtain driver so it can be deregistered
        Driver driver = null;
        try {
            driver = DriverManager.getDriver("jdbc:derby:;shutdown=true");
        } catch (SQLException se) {
            log.error("Unable to get Derby Driver", se);
        }

        // Shutdown Derby: is expected to throw SQLException XJ015
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException se) {
            if (((se.getErrorCode() == 50000) && ("XJ015".equals(se.getSQLState())))) {
                log.info("Derby shut down normally");
            } else {
                log.error("Derby did not shut down normally", se);
            }
        }

        // Deregister driver
        try {
            if (driver != null) {
                DriverManager.deregisterDriver(driver);
            } else {
                log.error("Unable to deregister Derby driver because the driver could not be obtained");
            }
        } catch (SQLException se) {
            log.error("Unable to deregister Derby driver", se);
        }
    }
}

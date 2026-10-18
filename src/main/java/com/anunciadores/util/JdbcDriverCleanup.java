package com.anunciadores.util;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Al redeplegar el .war en Tomcat, libera el driver de MySQL y su hilo de limpieza.
 * Sin esto Tomcat avisa de fugas de memoria ("mysql-cj-abandoned-connection-cleanup").
 */
@Component
public class JdbcDriverCleanup {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcDriverCleanup.class);

    @PreDestroy
    public void cleanup() {
        ClassLoader appClassLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            if (driver.getClass().getClassLoader() == appClassLoader) {
                try {
                    DriverManager.deregisterDriver(driver);
                } catch (SQLException e) {
                    LOGGER.warn("No se pudo liberar el driver JDBC {}", driver, e);
                }
            }
        }
        AbandonedConnectionCleanupThread.checkedShutdown();
    }
}

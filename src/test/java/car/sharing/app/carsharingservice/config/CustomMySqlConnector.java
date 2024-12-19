package car.sharing.app.carsharingservice.config;

import org.testcontainers.containers.MySQLContainer;

public class CustomMySqlConnector extends MySQLContainer<CustomMySqlConnector> {
    private static final String DB_IMAGE = "mysql:8";

    private static CustomMySqlConnector customMySqlConnector;

    private CustomMySqlConnector() {
        super(DB_IMAGE);
    }

    public static synchronized CustomMySqlConnector getInstance() {
        if (customMySqlConnector == null) {
            customMySqlConnector = new CustomMySqlConnector();
        }
        return customMySqlConnector;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("spring.datasource.url", getJdbcUrl());
        System.setProperty("spring.datasource.username", getUsername());
        System.setProperty("spring.datasource.password", getPassword());
    }

    @Override
    public void stop() {
        super.stop();
    }
}

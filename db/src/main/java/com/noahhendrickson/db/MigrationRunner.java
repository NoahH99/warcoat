package com.noahhendrickson.db;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MigrationRunner {

    private static final Logger LOG = LoggerFactory.getLogger(MigrationRunner.class);

    public static void main(String[] args) {
        LOG.info("Starting Flyway MigrationRunner...");

        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String pass = System.getenv("DB_PASS");

        if (url == null || user == null || pass == null) {
            LOG.error("Missing DB environment variables! (DB_URL, DB_USER, DB_PASS)");
            System.exit(1);
        }

        LOG.debug("DB_URL = {}", url);
        Flyway flyway = Flyway.configure()
                .dataSource(url, user, pass)
                .locations("classpath:db/migration")
                .load();

        try {
            flyway.migrate();
            LOG.info("Flyway migrations completed successfully.");
        } catch (Exception e) {
            LOG.error("Flyway migration failed!", e);
            System.exit(1);
        }
    }
}

package org.example.goajaspring.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Railway injects DATABASE_URL in libpq format: postgres://user:pass@host:port/db
 * Spring's JDBC driver requires:             jdbc:postgresql://user:pass@host:port/db
 *
 * This config rewrites the URL and builds the DataSource directly,
 * bypassing the application.properties datasource auto-configuration.
 */
@Configuration
public class DatabaseConfig {

    @Value("${DATABASE_URL}")
    private String databaseUrl;

    @Bean
    public DataSource dataSource() {
        String jdbcUrl = toJdbcUrl(databaseUrl);

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setDriverClassName("org.postgresql.Driver");
        config.setMaximumPoolSize(5);

        return new HikariDataSource(config);
    }

    private String toJdbcUrl(String url) {
        if (url == null) throw new IllegalStateException("DATABASE_URL is not set");

        // Already a valid JDBC URL — return as-is
        if (url.startsWith("jdbc:")) return url;

        // postgres:// or postgresql:// → jdbc:postgresql://
        return "jdbc:postgresql://" + url
                .replace("postgres://", "")
                .replace("postgresql://", "");
    }
}

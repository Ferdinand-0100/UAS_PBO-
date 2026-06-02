package org.example.goajaspring.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
public class DatabaseConfig {

    @Value("${DATABASE_URL}")
    private String databaseUrl;

    @Bean
    public DataSource dataSource() throws Exception {

        URI uri = URI.create(databaseUrl);

        String[] userInfo = uri.getUserInfo().split(":", 2);

        String username = userInfo[0];
        String password = userInfo[1];

        String jdbcUrl =
                "jdbc:postgresql://"
                        + uri.getHost()
                        + ":"
                        + uri.getPort()
                        + uri.getPath();

        System.out.println("JDBC URL = " + jdbcUrl);
        System.out.println("HOST = " + uri.getHost());

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("org.postgresql.Driver");

        return new HikariDataSource(config);
    }
}
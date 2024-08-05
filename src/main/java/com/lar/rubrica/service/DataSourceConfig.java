package com.lar.rubrica.service;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        Dotenv dotenv = Dotenv.load();
        String password_db = dotenv.get("PASSWORD_DB");
        String username = dotenv.get("USERNAME_DB");
        String url = dotenv.get("URL_DB");
        return new DriverManagerDataSource(
            url,
            username,
            password_db
        );
    }
}

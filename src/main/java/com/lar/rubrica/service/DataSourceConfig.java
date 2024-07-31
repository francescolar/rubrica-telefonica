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
        return new DriverManagerDataSource(
            "jdbc:postgresql://localhost:5432/rubrica",
            username,
            password_db
        );
    }
}

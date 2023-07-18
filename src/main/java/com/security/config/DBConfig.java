package com.security.config;

import com.security.utils.ENV;
import com.security.utils.EnvConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DBConfig {

    @Bean(name="coreDataSource")
    public DataSource coreDataSource() {
        HikariConfig config = new HikariConfig();

        config.setUsername(EnvConfig.getString(ENV.DB.CORE.USERNAME, "root"));
        config.setPassword(EnvConfig.getString(ENV.DB.CORE.PASSWORD, "root"));
        config.setDriverClassName(EnvConfig.getString(ENV.DB.CORE.DRIVER, "com.mysql.cj.jdbc.Driver"));
        config.setJdbcUrl(EnvConfig.getString(ENV.DB.CORE.URL, "jdbc:mysql://localhost:3306/jwt"));

        return new HikariDataSource(config);
    }

    @Bean(name="coreJdbcTemplate")
    public JdbcTemplate coreJdbcTemplate(@Qualifier("coreDataSource") DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        return jdbcTemplate;
    }

}


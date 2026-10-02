package com.project.ticketing_concurrency_lab.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties("app.datasource.mysql-a")
    public DataSourceProperties mysqlAProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public HikariDataSource mysqlADataSource(
            @Qualifier("mysqlAProperties")
            DataSourceProperties properties
    ) {
        return properties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }
    @Bean
    @ConfigurationProperties("app.datasource.mysql-issue")
    public DataSourceProperties mysqlBProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public HikariDataSource mysqlBDataSource(
            @Qualifier("mysqlBProperties")
            DataSourceProperties properties
    ) {
        return properties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }


}

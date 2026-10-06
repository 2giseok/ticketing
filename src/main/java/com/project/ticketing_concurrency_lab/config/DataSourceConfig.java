package com.project.ticketing_concurrency_lab.config;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

@Configuration
public class DataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties("app.datasource.mysql-a")
    public DataSourceProperties mysqlAProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public HikariDataSource mysqlADataSource(
            @Qualifier("mysqlAProperties")
            DataSourceProperties properties
    ) {
        return properties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    @ConfigurationProperties("app.datasource.mysql-b")
    public DataSourceProperties mysqlBProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public HikariDataSource mysqlBDataSource(
            @Qualifier("mysqlBProperties")
            DataSourceProperties properties
    ) {
        HikariDataSource dataSource = properties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();

        dataSource.setReadOnly(true);
        return dataSource;


    }

    @Bean
    @Primary
    public DataSource dataSource(
            @Qualifier("mysqlADataSource") DataSource writer,
            @Qualifier("mysqlBDataSource") DataSource reader
    ) {
        LazyConnectionDataSourceProxy dataSource =
                new LazyConnectionDataSourceProxy(writer);

        dataSource.setReadOnlyDataSource(reader);
        return dataSource;
    }


}

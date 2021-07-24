package com.allaroundjava.booking;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

@Configuration
public class IntegrationTestConfig {
    //dataSource bean is injected with @AutoConfigureEmbeddedDatabase annotation
    @Bean
    JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    DbCleaner dbCleaner(JdbcTemplate jdbcTemplate) {
        return new DbCleaner(jdbcTemplate);
    }

    @Primary
    @Bean
    Clock testClock() {
        return Clock.fixed(Instant.parse("2020-01-01T10:15:30.00Z"),
                ZoneOffset.UTC);
    }
}

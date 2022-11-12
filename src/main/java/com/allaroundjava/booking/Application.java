package com.allaroundjava.booking;

import com.allaroundjava.booking.config.BookingsConfig;
import com.allaroundjava.booking.config.DatabaseConfig;
import com.allaroundjava.booking.config.LoggingConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootConfiguration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@Import({DatabaseConfig.class, BookingsConfig.class, LoggingConfig.class})
@ServletComponentScan(basePackages = "com.allaroundjava.booking.adapters.api")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

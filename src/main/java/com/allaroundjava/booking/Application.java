package com.allaroundjava.booking;

import com.allaroundjava.booking.bookings.config.BookingsConfig;
import com.allaroundjava.booking.common.DatabaseConfig;
import com.allaroundjava.booking.common.events.EventsConfig;
import com.allaroundjava.booking.items.ItemsConfig;
import com.allaroundjava.booking.owners.OwnersConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootConfiguration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@Import({DatabaseConfig.class, EventsConfig.class, OwnersConfig.class, ItemsConfig.class, BookingsConfig.class})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

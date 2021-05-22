package com.allaroundjava.booking.bookings.config;

import com.allaroundjava.booking.bookings.domain.ports.AvailabilitiesRepository;
import com.allaroundjava.booking.bookings.domain.ports.AvailabilitiesService;
import com.allaroundjava.booking.bookings.domain.ports.OccupationRepository;
import com.allaroundjava.booking.bookings.domain.ports.OccupationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.allaroundjava.booking.bookings.adapters")
public class BookingsConfig {

    @Bean
    AvailabilitiesService availabilitiesService(AvailabilitiesRepository repository){
        return new AvailabilitiesService(repository);
    }

    @Bean
    OccupationService occupationService(OccupationRepository occupationRepository) {
        return new OccupationService(occupationRepository);
    }
}

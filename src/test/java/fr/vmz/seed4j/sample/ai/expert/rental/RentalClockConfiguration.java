package fr.vmz.seed4j.sample.ai.expert.rental;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class RentalClockConfiguration {

  @Bean
  @Primary
  RentalClock rentalClock() {
    return new RentalClock();
  }
}

package fr.vmz.seed4j.sample.ai.advanced.shop.infrastructure.primary;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
class MovableClockConfiguration {

  @Bean
  @Primary
  MovableClock movableClock() {
    return new MovableClock();
  }
}

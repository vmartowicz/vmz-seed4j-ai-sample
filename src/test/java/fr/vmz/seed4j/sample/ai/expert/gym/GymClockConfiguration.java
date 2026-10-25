package fr.vmz.seed4j.sample.ai.expert.gym;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class GymClockConfiguration {

  @Bean
  @Primary
  GymClock gymClock() {
    return new GymClock();
  }
}

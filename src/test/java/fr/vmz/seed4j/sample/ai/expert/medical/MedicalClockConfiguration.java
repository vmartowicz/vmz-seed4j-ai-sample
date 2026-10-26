package fr.vmz.seed4j.sample.ai.expert.medical;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class MedicalClockConfiguration {

  @Bean
  @Primary
  MedicalClock medicalClock() {
    return new MedicalClock();
  }
}

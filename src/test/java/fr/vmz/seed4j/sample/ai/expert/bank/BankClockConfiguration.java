package fr.vmz.seed4j.sample.ai.expert.bank;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class BankClockConfiguration {

  @Bean
  @Primary
  BankClock bankClock() {
    return new BankClock();
  }
}

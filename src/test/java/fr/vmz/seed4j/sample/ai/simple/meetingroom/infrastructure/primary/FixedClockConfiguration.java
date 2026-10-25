package fr.vmz.seed4j.sample.ai.simple.meetingroom.infrastructure.primary;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
class FixedClockConfiguration {

  // Now is Tuesday 2026-03-10 at 10:30 in Paris
  @Bean
  @Primary
  Clock fixedClock() {
    return Clock.fixed(Instant.parse("2026-03-10T09:30:00Z"), ZoneId.of("Europe/Paris"));
  }
}

package fr.vmz.seed4j.sample.ai.wire.clock.infrastructure.primary;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;

@UnitTest
class ClockConfigurationTest {

  @Test
  void shouldUseParisTimeZone() {
    assertThat(new ClockConfiguration().clock().getZone()).isEqualTo(ZoneId.of("Europe/Paris"));
  }
}

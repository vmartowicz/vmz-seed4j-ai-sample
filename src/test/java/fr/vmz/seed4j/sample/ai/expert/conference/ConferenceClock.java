package fr.vmz.seed4j.sample.ai.expert.conference;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public final class ConferenceClock extends Clock {

  private static final ZoneId PARIS = ZoneId.of("Europe/Paris");

  private Instant instant = Instant.parse("2026-03-10T09:00:00Z");

  public void at(LocalDateTime now) {
    instant = now.atZone(PARIS).toInstant();
  }

  @Override
  public ZoneId getZone() {
    return PARIS;
  }

  @Override
  public Clock withZone(ZoneId zone) {
    return Clock.fixed(instant, zone);
  }

  @Override
  public Instant instant() {
    return instant;
  }
}

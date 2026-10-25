package fr.vmz.seed4j.sample.ai.advanced.shop.infrastructure.primary;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

final class MovableClock extends Clock {

  private static final ZoneId PARIS = ZoneId.of("Europe/Paris");

  private Instant instant = Instant.parse("2026-03-10T09:00:00Z");

  void today(LocalDate today) {
    instant = today.atTime(10, 0).atZone(PARIS).toInstant();
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

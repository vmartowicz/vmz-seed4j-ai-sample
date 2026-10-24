package fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog;

import java.time.Duration;
import java.util.UUID;

public final class CatalogFixture {

  public static final BookableService HAIRCUT = new BookableService(
    new ServiceId(UUID.fromString("00000000-0000-4000-8000-000000000001")),
    new ServiceName("Haircut"),
    Duration.ofMinutes(30)
  );

  public static final BookableService MASSAGE = new BookableService(
    new ServiceId(UUID.fromString("00000000-0000-4000-8000-000000000002")),
    new ServiceName("Massage"),
    Duration.ofHours(1)
  );

  private CatalogFixture() {}
}

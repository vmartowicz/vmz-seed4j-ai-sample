package fr.vmz.seed4j.sample.ai.simple.booking.infrastructure.secondary.catalog;

import fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog.BookableService;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog.ServiceId;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog.ServiceName;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog.ServicesCatalog;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryServicesCatalog implements ServicesCatalog {

  private final List<BookableService> services = List.of(
    service("00000000-0000-4000-8000-000000000001", "Haircut", Duration.ofMinutes(30)),
    service("00000000-0000-4000-8000-000000000002", "Massage", Duration.ofHours(1))
  );

  private static BookableService service(String id, String name, Duration duration) {
    return new BookableService(new ServiceId(UUID.fromString(id)), new ServiceName(name), duration);
  }

  @Override
  public List<BookableService> all() {
    return services;
  }

  @Override
  public Optional<BookableService> get(ServiceId service) {
    return services
      .stream()
      .filter(candidate -> candidate.id().equals(service))
      .findFirst();
  }
}

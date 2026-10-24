package fr.vmz.seed4j.sample.ai.simple.booking.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog.BookableService;
import java.util.UUID;

record RestBookableService(UUID id, String name, long durationInMinutes) {
  static RestBookableService from(BookableService service) {
    return new RestBookableService(service.id().get(), service.name().get(), service.duration().toMinutes());
  }
}

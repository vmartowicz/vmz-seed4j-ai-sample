package fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog;

import java.util.List;
import java.util.Optional;

public interface ServicesCatalog {
  List<BookableService> all();

  Optional<BookableService> get(ServiceId service);

  default BookableService service(ServiceId service) {
    return get(service).orElseThrow(() -> new UnknownServiceException(service));
  }
}

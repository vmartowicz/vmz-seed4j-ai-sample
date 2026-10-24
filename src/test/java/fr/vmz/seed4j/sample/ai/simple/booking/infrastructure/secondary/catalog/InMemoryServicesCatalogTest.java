package fr.vmz.seed4j.sample.ai.simple.booking.infrastructure.secondary.catalog;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog.ServiceId;
import java.time.Duration;
import java.util.UUID;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryServicesCatalogTest {

  private final InMemoryServicesCatalog catalog = new InMemoryServicesCatalog();

  @Test
  void shouldListServices() {
    assertThat(catalog.all()).extracting(service -> service.name().get()).containsExactly("Haircut", "Massage");
  }

  @Test
  void shouldGetServiceById() {
    var massage = catalog.all().get(1);

    assertThat(catalog.get(massage.id())).contains(massage);
    assertThat(massage.duration()).isEqualTo(Duration.ofHours(1));
    assertThat(catalog.get(new ServiceId(UUID.randomUUID()))).isEmpty();
  }
}

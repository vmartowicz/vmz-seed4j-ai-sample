package fr.vmz.seed4j.sample.ai.advanced.restaurant.infrastructure.secondary.diner;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner.Diner;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner.DinerEmail;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryDinersRepositoryTest {

  private final InMemoryDinersRepository diners = new InMemoryDinersRepository();

  @Test
  void shouldGetSavedDinerByEmail() {
    var diner = Diner.newcomer(new DinerEmail("jane@mail.com"));

    diners.save(diner);

    assertThat(diners.get(new DinerEmail("JANE@mail.com"))).containsSame(diner);
  }

  @Test
  void shouldNotGetUnknownDiner() {
    assertThat(diners.get(new DinerEmail("unknown@mail.com"))).isEmpty();
  }
}

package fr.vmz.seed4j.sample.ai.expert.rental.billing.infrastructure.secondary.deposit;

import static fr.vmz.seed4j.sample.ai.expert.rental.billing.domain.RentalBillingFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import java.util.UUID;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryDepositsRepositoryTest {

  private final InMemoryDepositsRepository deposits = new InMemoryDepositsRepository();

  @Test
  void shouldGetSavedDeposit() {
    var deposit = held();

    deposits.save(deposit);

    assertThat(deposits.get(deposit.contract())).containsSame(deposit);
    assertThat(deposits.get(UUID.randomUUID())).isEmpty();
    assertThat(deposits.all()).containsExactly(deposit);
  }
}

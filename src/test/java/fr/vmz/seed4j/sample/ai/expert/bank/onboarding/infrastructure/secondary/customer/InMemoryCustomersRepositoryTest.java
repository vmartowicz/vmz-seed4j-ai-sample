package fr.vmz.seed4j.sample.ai.expert.bank.onboarding.infrastructure.secondary.customer;

import static fr.vmz.seed4j.sample.ai.expert.bank.onboarding.domain.OnboardingFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryCustomersRepositoryTest {

  private final InMemoryCustomersRepository customers = new InMemoryCustomersRepository();

  @Test
  void shouldGetSavedCustomers() {
    var customer = customer();

    customers.save(customer);

    assertThat(customers.get(customer.id())).containsSame(customer);
    assertThat(customers.get(CustomerId.newId())).isEmpty();
    assertThat(customers.all()).containsExactly(customer);
  }
}

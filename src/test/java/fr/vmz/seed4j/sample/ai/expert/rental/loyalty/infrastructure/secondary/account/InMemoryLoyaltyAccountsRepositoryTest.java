package fr.vmz.seed4j.sample.ai.expert.rental.loyalty.infrastructure.secondary.account;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.rental.loyalty.domain.LoyaltyAccount;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryLoyaltyAccountsRepositoryTest {

  private final InMemoryLoyaltyAccountsRepository accounts = new InMemoryLoyaltyAccountsRepository();

  @Test
  void shouldGetSavedAccount() {
    var account = LoyaltyAccount.open(new Email("jane@mail.com"));

    accounts.save(account);

    assertThat(accounts.get(new Email("jane@mail.com"))).containsSame(account);
    assertThat(accounts.get(new Email("john@mail.com"))).isEmpty();
  }
}

package fr.vmz.seed4j.sample.ai.expert.gym.billing.infrastructure.secondary.account;

import static fr.vmz.seed4j.sample.ai.expert.gym.billing.domain.BillingFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.gym.billing.domain.BillingAccount;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryBillingAccountsRepositoryTest {

  private final InMemoryBillingAccountsRepository accounts = new InMemoryBillingAccountsRepository();

  @Test
  void shouldGetSavedAccounts() {
    var account = BillingAccount.open(MemberId.newId(), euros("29.90"));

    accounts.save(account);

    assertThat(accounts.get(account.member())).containsSame(account);
    assertThat(accounts.get(MemberId.newId())).isEmpty();
    assertThat(accounts.all()).containsExactly(account);
  }
}

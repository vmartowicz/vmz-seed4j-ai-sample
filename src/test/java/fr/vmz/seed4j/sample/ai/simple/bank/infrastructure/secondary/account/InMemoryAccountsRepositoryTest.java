package fr.vmz.seed4j.sample.ai.simple.bank.infrastructure.secondary.account;

import static fr.vmz.seed4j.sample.ai.simple.bank.domain.AccountsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.simple.bank.domain.AccountId;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryAccountsRepositoryTest {

  private final InMemoryAccountsRepository accounts = new InMemoryAccountsRepository();

  @Test
  void shouldGetSavedAccount() {
    var account = account();
    accounts.save(account);

    assertThat(accounts.get(account.id())).contains(account);
  }

  @Test
  void shouldNotGetUnknownAccount() {
    assertThat(accounts.get(AccountId.newId())).isEmpty();
  }
}

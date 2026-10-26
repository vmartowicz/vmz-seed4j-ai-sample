package fr.vmz.seed4j.sample.ai.expert.bank.accounts.infrastructure.secondary.account;

import static fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.AccountsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryBankAccountsRepositoryTest {

  private final InMemoryBankAccountsRepository accounts = new InMemoryBankAccountsRepository();

  @Test
  void shouldFindAccountsOfHolder() {
    var account = current("1000");
    accounts.save(account);
    accounts.save(savings());

    assertThat(accounts.get(account.iban())).containsSame(account);
    assertThat(accounts.get(Iban.generate())).isEmpty();
    assertThat(accounts.ofHolder(account.holders().getFirst())).containsExactly(account);
    assertThat(accounts.ofHolder(CustomerId.newId())).isEmpty();
    assertThat(accounts.all()).hasSize(2);
  }
}

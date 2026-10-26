package fr.vmz.seed4j.sample.ai.expert.bank.compliance.infrastructure.secondary.accounts;

import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.bank.accounts.infrastructure.primary.JavaAccounts;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import org.junit.jupiter.api.Test;

@UnitTest
class ComplianceSuspiciousAccountsTest {

  @Test
  void shouldFreezeAccount() {
    var accounts = mock(JavaAccounts.class);
    var iban = Iban.generate();

    new ComplianceSuspiciousAccounts(accounts).freeze(iban);

    verify(accounts).freezeForSanctions(iban.get());
  }
}

package fr.vmz.seed4j.sample.ai.expert.bank.accounts.infrastructure.primary;

import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.bank.accounts.application.RetailAccountsApplicationService;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import org.junit.jupiter.api.Test;

@UnitTest
class JavaAccountsTest {

  @Test
  void shouldFreezeForSanctions() {
    var accounts = mock(RetailAccountsApplicationService.class);
    var iban = Iban.generate();

    new JavaAccounts(accounts).freezeForSanctions(iban.get());

    verify(accounts).freezeForSanctions(iban);
  }
}

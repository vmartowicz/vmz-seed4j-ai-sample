package fr.vmz.seed4j.sample.ai.expert.bank.accounts.infrastructure.primary;

import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.bank.accounts.application.RetailAccountsApplicationService;
import org.junit.jupiter.api.Test;

@UnitTest
class AccountClosingJobsTest {

  @Test
  void shouldAccrueInterestAndCloseMonth() {
    var accounts = mock(RetailAccountsApplicationService.class);
    var jobs = new AccountClosingJobs(accounts);

    jobs.accrueInterest();
    jobs.closeMonth();

    verify(accounts).accrueInterest();
    verify(accounts).closeMonth();
  }
}

package fr.vmz.seed4j.sample.ai.expert.bank.lending.infrastructure.primary;

import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.bank.lending.application.LendingApplicationService;
import org.junit.jupiter.api.Test;

@UnitTest
class LoanJobsTest {

  @Test
  void shouldDisburseLoansAndCollectInstalments() {
    var lending = mock(LendingApplicationService.class);

    new LoanJobs(lending).run();

    verify(lending).disburse();
    verify(lending).collectInstalments();
  }
}

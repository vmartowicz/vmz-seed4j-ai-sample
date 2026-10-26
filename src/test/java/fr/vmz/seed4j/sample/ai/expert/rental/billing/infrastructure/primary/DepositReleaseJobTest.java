package fr.vmz.seed4j.sample.ai.expert.rental.billing.infrastructure.primary;

import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.rental.billing.application.RentalBillingApplicationService;
import org.junit.jupiter.api.Test;

@UnitTest
class DepositReleaseJobTest {

  @Test
  void shouldReleaseDueDeposits() {
    var billing = mock(RentalBillingApplicationService.class);

    new DepositReleaseJob(billing).release();

    verify(billing).releaseDeposits();
  }
}

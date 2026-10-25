package fr.vmz.seed4j.sample.ai.expert.gym.billing.infrastructure.primary;

import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.gym.billing.application.BillingApplicationService;
import org.junit.jupiter.api.Test;

@UnitTest
class DailyBillingJobTest {

  @Test
  void shouldRunDailyBilling() {
    var billing = mock(BillingApplicationService.class);

    new DailyBillingJob(billing).run();

    verify(billing).runDaily();
  }
}

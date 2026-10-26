package fr.vmz.seed4j.sample.ai.expert.medical.billing.infrastructure.primary;

import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.medical.billing.application.MedicalBillingApplicationService;
import org.junit.jupiter.api.Test;

@UnitTest
class CareSheetsJobTest {

  @Test
  void shouldSendPendingCareSheets() {
    var billing = mock(MedicalBillingApplicationService.class);

    new CareSheetsJob(billing).send();

    verify(billing).sendPendingCareSheets();
  }
}

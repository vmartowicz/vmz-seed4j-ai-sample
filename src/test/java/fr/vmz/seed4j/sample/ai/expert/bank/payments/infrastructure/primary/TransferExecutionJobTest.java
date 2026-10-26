package fr.vmz.seed4j.sample.ai.expert.bank.payments.infrastructure.primary;

import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.bank.payments.application.PaymentsApplicationService;
import org.junit.jupiter.api.Test;

@UnitTest
class TransferExecutionJobTest {

  @Test
  void shouldExecuteDueTransfers() {
    var payments = mock(PaymentsApplicationService.class);

    new TransferExecutionJob(payments).execute();

    verify(payments).executeDueTransfers();
  }
}

package fr.vmz.seed4j.sample.ai.expert.conference.cfp.infrastructure.primary;

import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.conference.cfp.application.CallForPapersApplicationService;
import org.junit.jupiter.api.Test;

@UnitTest
class ConfirmationDeadlinesJobTest {

  @Test
  void shouldExpireUnconfirmedProposals() {
    var cfp = mock(CallForPapersApplicationService.class);

    new ConfirmationDeadlinesJob(cfp).expire();

    verify(cfp).expireUnconfirmed();
  }
}

package fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.infrastructure.primary;

import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.application.SponsoringApplicationService;
import org.junit.jupiter.api.Test;

@UnitTest
class UnpaidSponsorshipsJobTest {

  @Test
  void shouldReleaseUnpaidSponsorships() {
    var sponsoring = mock(SponsoringApplicationService.class);

    new UnpaidSponsorshipsJob(sponsoring).release();

    verify(sponsoring).releaseUnpaid();
  }
}

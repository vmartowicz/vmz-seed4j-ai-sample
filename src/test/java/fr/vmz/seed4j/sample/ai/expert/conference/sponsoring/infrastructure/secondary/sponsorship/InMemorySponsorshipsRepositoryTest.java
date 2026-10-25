package fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.infrastructure.secondary.sponsorship;

import static fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain.SponsorshipsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain.Pack;
import fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain.SponsorId;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemorySponsorshipsRepositoryTest {

  private final InMemorySponsorshipsRepository sponsorships = new InMemorySponsorshipsRepository();

  @Test
  void shouldGetSavedSponsorships() {
    var sponsorship = reserved(Pack.GOLD);

    sponsorships.save(sponsorship);

    assertThat(sponsorships.get(sponsorship.id())).containsSame(sponsorship);
    assertThat(sponsorships.get(SponsorId.newId())).isEmpty();
    assertThat(sponsorships.all()).containsExactly(sponsorship);
  }
}

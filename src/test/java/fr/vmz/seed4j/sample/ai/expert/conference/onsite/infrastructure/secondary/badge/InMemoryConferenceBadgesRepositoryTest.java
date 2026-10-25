package fr.vmz.seed4j.sample.ai.expert.conference.onsite.infrastructure.secondary.badge;

import static fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.OnsiteFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.BadgeId;
import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.BadgeType;
import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.ConferenceBadge;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryConferenceBadgesRepositoryTest {

  private final InMemoryConferenceBadgesRepository badges = new InMemoryConferenceBadgesRepository();

  @Test
  void shouldFindBadgesOfTicket() {
    var badge = badge("jane", BadgeType.ATTENDEE);
    badges.save(badge);
    badges.save(ConferenceBadge.volunteer("Ada"));
    badges.save(badge("john", BadgeType.ATTENDEE));

    assertThat(badges.get(badge.id())).containsSame(badge);
    assertThat(badges.get(BadgeId.newId())).isEmpty();
    assertThat(badges.ofTicket("RC-JANE")).containsExactly(badge);
  }
}

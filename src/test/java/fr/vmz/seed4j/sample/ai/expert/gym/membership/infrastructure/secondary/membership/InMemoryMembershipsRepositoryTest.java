package fr.vmz.seed4j.sample.ai.expert.gym.membership.infrastructure.secondary.membership;

import static fr.vmz.seed4j.sample.ai.expert.gym.membership.domain.MembershipsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryMembershipsRepositoryTest {

  private final InMemoryMembershipsRepository memberships = new InMemoryMembershipsRepository();

  @Test
  void shouldGetSavedMembership() {
    var membership = activeMembership();

    memberships.save(membership);

    assertThat(memberships.get(membership.id())).containsSame(membership);
    assertThat(memberships.get(MemberId.newId())).isEmpty();
  }
}

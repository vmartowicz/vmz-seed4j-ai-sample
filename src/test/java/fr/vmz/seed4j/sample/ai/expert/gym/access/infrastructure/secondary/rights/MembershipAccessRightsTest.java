package fr.vmz.seed4j.sample.ai.expert.gym.access.infrastructure.secondary.rights;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.gym.access.domain.AccessRights;
import fr.vmz.seed4j.sample.ai.expert.gym.access.domain.MemberStatus;
import fr.vmz.seed4j.sample.ai.expert.gym.billing.infrastructure.primary.JavaBilling;
import fr.vmz.seed4j.sample.ai.expert.gym.membership.infrastructure.primary.JavaMembership;
import fr.vmz.seed4j.sample.ai.expert.gym.membership.infrastructure.primary.JavaMemberships;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;

@UnitTest
class MembershipAccessRightsTest {

  private final JavaMemberships memberships = mock(JavaMemberships.class);
  private final JavaBilling billing = mock(JavaBilling.class);
  private final MembershipAccessRights rights = new MembershipAccessRights(memberships, billing);
  private final MemberId member = MemberId.newId();

  @Test
  void shouldGiveActiveMemberRights() {
    membership(true, false);
    doReturn(true).when(billing).isInArrears(member.get());

    assertThat(rights.rightsOf(member)).isEqualTo(new AccessRights(MemberStatus.ACTIVE, true, Set.of(Gym.ECULLY)));
  }

  @Test
  void shouldGiveSuspendedMemberRights() {
    membership(false, true);

    assertThat(rights.rightsOf(member).status()).isEqualTo(MemberStatus.SUSPENDED);
  }

  @Test
  void shouldGiveInactiveMemberRights() {
    membership(false, false);

    assertThat(rights.rightsOf(member).status()).isEqualTo(MemberStatus.INACTIVE);
  }

  @Test
  void shouldGiveNoRightsToUnknownMember() {
    doReturn(Optional.empty()).when(memberships).membership(member.get());

    assertThat(rights.rightsOf(member)).isEqualTo(new AccessRights(MemberStatus.INACTIVE, false, Set.of()));
  }

  private void membership(boolean active, boolean suspended) {
    doReturn(Optional.of(new JavaMembership(active, suspended, false, Gym.ECULLY, Set.of(Gym.ECULLY)))).when(memberships).membership(member.get());
  }
}

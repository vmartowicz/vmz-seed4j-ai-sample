package fr.vmz.seed4j.sample.ai.expert.gym.access.domain;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class TurnstilesTest {

  private static final LocalDateTime NOW = LocalDateTime.of(2026, 3, 10, 18, 0);

  private final BadgesRepository badges = mock(BadgesRepository.class, CALLS_REAL_METHODS);
  private final PresencesRepository presences = mock(PresencesRepository.class);
  private final AccessRightsProvider rights = mock(AccessRightsProvider.class);
  private final Turnstiles turnstiles = new Turnstiles(badges, presences, rights);

  private final MemberId member = MemberId.newId();
  private final Badge badge = new Badge(BadgeNumber.generate(), member);

  @BeforeEach
  void loadBadge() {
    doReturn(Optional.of(badge)).when(badges).get(badge.number());
    doReturn(Optional.empty()).when(presences).openOf(member);
    doReturn(List.of()).when(presences).openIn(any());
    rights(MemberStatus.ACTIVE, false, Set.of(Gym.ECULLY));
  }

  @Test
  void shouldIssueBadge() {
    var issued = turnstiles.issueBadge(member);

    assertThat(issued.member()).isEqualTo(member);
    verify(badges).save(issued);
  }

  @Nested
  class Enter {

    @Test
    void shouldLetActiveMemberIntoIncludedGym() {
      var entry = turnstiles.enter(badge.number(), Gym.ECULLY, NOW);

      assertThat(entry.accepted()).isTrue();
      verify(presences).save(argThat(presence -> presence.member().equals(member) && presence.gym() == Gym.ECULLY && presence.isOpen()));
    }

    @Test
    void shouldRefuseSuspendedMember() {
      rights(MemberStatus.SUSPENDED, false, Set.of(Gym.ECULLY));

      assertRefused(RefusalReason.SUSPENDED);
    }

    @Test
    void shouldRefuseInactiveMember() {
      rights(MemberStatus.INACTIVE, false, Set.of(Gym.ECULLY));

      assertRefused(RefusalReason.MEMBERSHIP_INACTIVE);
    }

    @Test
    void shouldRefuseMemberInArrears() {
      rights(MemberStatus.ACTIVE, true, Set.of(Gym.ECULLY));

      assertRefused(RefusalReason.IN_ARREARS);
    }

    @Test
    void shouldRefuseGymNotIncludedInPlan() {
      rights(MemberStatus.ACTIVE, false, Set.of(Gym.CONFLUENCE));

      assertRefused(RefusalReason.GYM_NOT_INCLUDED);
    }

    @Test
    void shouldRefuseBadgeAlreadyInside() {
      doReturn(Optional.of(new Presence(member, Gym.ECULLY, NOW.minusHours(3)))).when(presences).openOf(member);

      assertRefused(RefusalReason.ALREADY_INSIDE);
    }

    @Test
    void shouldCloseExpiredPresenceAndLetIn() {
      var forgotten = new Presence(member, Gym.ECULLY, NOW.minusHours(4));
      doReturn(Optional.of(forgotten)).when(presences).openOf(member);

      assertThat(turnstiles.enter(badge.number(), Gym.ECULLY, NOW).accepted()).isTrue();

      assertThat(forgotten.exitedAt()).contains(NOW);
      verify(presences).save(forgotten);
    }

    @Test
    void shouldRefuseFullGym() {
      doReturn(inside(100, NOW.minusHours(1))).when(presences).openIn(Gym.ECULLY);

      assertRefused(RefusalReason.GYM_FULL);
    }

    @Test
    void shouldNotCountExpiredPresencesInOccupancy() {
      doReturn(inside(100, NOW.minusHours(5))).when(presences).openIn(Gym.ECULLY);

      assertThat(turnstiles.enter(badge.number(), Gym.ECULLY, NOW).accepted()).isTrue();
    }

    @Test
    void shouldNotEnterWithUnknownBadge() {
      var unknown = BadgeNumber.generate();
      doReturn(Optional.empty()).when(badges).get(unknown);

      assertThatThrownBy(() -> turnstiles.enter(unknown, Gym.ECULLY, NOW)).isExactlyInstanceOf(UnknownBadgeException.class);
    }

    private void assertRefused(RefusalReason reason) {
      assertThat(turnstiles.enter(badge.number(), Gym.ECULLY, NOW)).isEqualTo(Entry.refused(reason));
      verify(presences, never()).save(argThat(Presence::isOpen));
    }
  }

  @Nested
  class Exit {

    @Test
    void shouldCloseOpenPresence() {
      var presence = new Presence(member, Gym.ECULLY, NOW.minusHours(1));
      doReturn(Optional.of(presence)).when(presences).openOf(member);

      assertThat(turnstiles.exit(badge.number(), NOW)).isSameAs(presence);

      assertThat(presence.exitedAt()).contains(NOW);
      verify(presences).save(presence);
    }

    @Test
    void shouldNotExitWithoutEntering() {
      assertThatThrownBy(() -> turnstiles.exit(badge.number(), NOW)).isExactlyInstanceOf(NotInsideException.class);
    }
  }

  @Test
  void shouldCountPeopleInside() {
    doReturn(Collections.unmodifiableList(inside(3, NOW.minusHours(1)))).when(presences).openIn(Gym.PART_DIEU);

    var occupancy = turnstiles.occupancy(Gym.PART_DIEU, NOW);

    assertThat(occupancy).isEqualTo(new Occupancy(Gym.PART_DIEU, 3, 150));
  }

  private void rights(MemberStatus status, boolean inArrears, Set<Gym> gyms) {
    doReturn(new AccessRights(status, inArrears, gyms)).when(rights).rightsOf(member);
  }

  private static List<Presence> inside(int people, LocalDateTime enteredAt) {
    return IntStream.range(0, people).mapToObj(person -> new Presence(MemberId.newId(), Gym.ECULLY, enteredAt)).toList();
  }
}

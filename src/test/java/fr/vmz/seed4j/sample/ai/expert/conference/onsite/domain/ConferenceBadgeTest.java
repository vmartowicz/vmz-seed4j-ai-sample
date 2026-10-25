package fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain;

import static fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.OnsiteFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Room;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import org.junit.jupiter.api.Test;

@UnitTest
class ConferenceBadgeTest {

  @Test
  void shouldPrintBadgeOfTicketHolder() {
    var badge = badge("jane", BadgeType.SPEAKER);

    assertThat(badge.id()).isNotNull();
    assertThat(badge.name()).isEqualTo("jane");
    assertThat(badge.email()).contains(new Email("jane@mail.com"));
    assertThat(badge.type()).isEqualTo(BadgeType.SPEAKER);
    assertThat(badge.ticket()).contains("RC-JANE");
    assertThat(badge.isActive()).isTrue();
  }

  @Test
  void shouldPrintVolunteerBadgeWithoutTicket() {
    var badge = ConferenceBadge.volunteer("Ada");

    assertThat(badge.type()).isEqualTo(BadgeType.VOLUNTEER);
    assertThat(badge.ticket()).isEmpty();
    assertThat(badge.email()).isEmpty();
  }

  @Test
  void shouldNotPrintVolunteerBadgeWithoutName() {
    assertThatThrownBy(() -> ConferenceBadge.volunteer(" ")).isInstanceOf(AssertionException.class);
  }

  @Test
  void shouldDeactivateBadge() {
    var badge = badge("jane", BadgeType.ATTENDEE);

    badge.deactivate();

    assertThat(badge.isActive()).isFalse();
  }

  @Test
  void shouldKnowSpeakersOfSession() {
    var workshop = workshopBy("jane");

    assertThat(badge("jane", BadgeType.SPEAKER).speaksIn(workshop)).isTrue();
    assertThat(badge("john", BadgeType.SPEAKER).speaksIn(workshop)).isFalse();
    assertThat(badge("jane", BadgeType.ATTENDEE).speaksIn(workshop)).isFalse();
    assertThat(ConferenceBadge.volunteer("Ada").speaksIn(workshop)).isFalse();
  }

  @Test
  void shouldTrackPresenceInRoom() {
    var presence = new RoomPresence(badge("jane", BadgeType.ATTENDEE).id(), Room.SAONE, NOW, talk("Kotlin"));

    assertThat(presence.isOpen()).isTrue();
    assertThat(presence.session()).isPresent();

    presence.exit(NOW.plusMinutes(30));

    assertThat(presence.isOpen()).isFalse();
    assertThat(presence.exitedAt()).contains(NOW.plusMinutes(30));
  }

  @Test
  void shouldExplainRefusals() {
    assertThat(RefusalReason.ROOM_FULL.message()).contains("live stream");
    assertThat(RoomEntry.welcome().accepted()).isTrue();
    assertThat(RoomEntry.refused(RefusalReason.NOT_REGISTERED).refusal()).contains(RefusalReason.NOT_REGISTERED);
  }
}

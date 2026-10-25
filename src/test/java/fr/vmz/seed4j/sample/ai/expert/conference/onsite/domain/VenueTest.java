package fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain;

import static fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.OnsiteFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Room;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class VenueTest {

  private final ConferenceBadgesRepository badges = mock(ConferenceBadgesRepository.class, CALLS_REAL_METHODS);
  private final RoomPresencesRepository presences = mock(RoomPresencesRepository.class);
  private final TicketsDesk tickets = mock(TicketsDesk.class);
  private final RoomSessions sessions = mock(RoomSessions.class);
  private final WorkshopRegistrations registrations = mock(WorkshopRegistrations.class);
  private final Venue venue = new Venue(badges, presences, tickets, sessions, registrations);

  private final List<RoomPresence> all = new ArrayList<>();

  @BeforeEach
  void loadVenue() {
    doReturn(List.of()).when(badges).ofTicket(any());
    doReturn(Optional.empty()).when(presences).openOf(any());
    doReturn(List.of()).when(presences).openIn(any());
    doReturn(all).when(presences).all();
    doReturn(Optional.empty()).when(sessions).sessionAt(any(), any());
  }

  @Nested
  class Badges {

    @Test
    void shouldPrintBadgeFromValidTicket() {
      doReturn(Optional.of(ticket("jane", BadgeType.ATTENDEE))).when(tickets).ticket("RC-JANE");

      var badge = venue.printBadge("RC-JANE");

      assertThat(badge.name()).isEqualTo("jane");
      verify(badges).save(badge);
    }

    @Test
    void shouldDeactivatePreviousBadgeOfTicket() {
      var previous = badge("jane", BadgeType.ATTENDEE);
      var alreadyInactive = badge("jane", BadgeType.ATTENDEE);
      alreadyInactive.deactivate();
      doReturn(List.of(previous, alreadyInactive)).when(badges).ofTicket("RC-JANE");
      doReturn(Optional.of(ticket("jane", BadgeType.ATTENDEE))).when(tickets).ticket("RC-JANE");

      var reprinted = venue.printBadge("RC-JANE");

      assertThat(previous.isActive()).isFalse();
      assertThat(reprinted.isActive()).isTrue();
      verify(badges).save(previous);
      verify(badges, never()).save(alreadyInactive);
    }

    @Test
    void shouldNotPrintBadgeOfInvalidTicket() {
      doReturn(Optional.of(new TicketView("RC-JANE", "jane", new Email("jane@mail.com"), BadgeType.ATTENDEE, false))).when(tickets).ticket("RC-JANE");

      assertThatThrownBy(() -> venue.printBadge("RC-JANE")).isExactlyInstanceOf(TicketNotValidForBadgeException.class);
    }

    @Test
    void shouldNotPrintBadgeOfUnknownTicket() {
      doReturn(Optional.empty()).when(tickets).ticket("RC-NONE");

      assertThatThrownBy(() -> venue.printBadge("RC-NONE")).isExactlyInstanceOf(UnknownTicketForBadgeException.class);
    }

    @Test
    void shouldPrintVolunteerBadge() {
      var badge = venue.volunteerBadge("Ada");

      assertThat(badge.type()).isEqualTo(BadgeType.VOLUNTEER);
      verify(badges).save(badge);
    }

    @Test
    void shouldNotFindUnknownBadge() {
      var unknown = BadgeId.newId();
      doReturn(Optional.empty()).when(badges).get(unknown);

      assertThatThrownBy(() -> venue.badge(unknown)).isExactlyInstanceOf(UnknownConferenceBadgeException.class);
    }
  }

  @Nested
  class Enter {

    @Test
    void shouldEnterRoomDuringTalk() {
      var badge = known(badge("jane", BadgeType.ATTENDEE));
      var talk = talk("Kotlin");
      doReturn(Optional.of(talk)).when(sessions).sessionAt(Room.SAONE, NOW);

      assertThat(venue.enter(badge.id(), Room.SAONE, NOW).accepted()).isTrue();

      verify(presences).save(argThat(presence -> presence.room() == Room.SAONE && presence.session().orElseThrow().equals(talk)));
    }

    @Test
    void shouldLeavePreviousRoomWhenEnteringAnother() {
      var badge = known(badge("jane", BadgeType.ATTENDEE));
      var previous = new RoomPresence(badge.id(), Room.AMPHI, NOW.minusHours(1), null);
      doReturn(Optional.of(previous)).when(presences).openOf(badge.id());

      venue.enter(badge.id(), Room.SAONE, NOW);

      assertThat(previous.exitedAt()).contains(NOW);
      verify(presences).save(previous);
    }

    @Test
    void shouldRefuseFullRoom() {
      var badge = known(badge("jane", BadgeType.ATTENDEE));
      doReturn(inside(Room.CROIX_ROUSSE, 40)).when(presences).openIn(Room.CROIX_ROUSSE);

      assertThat(venue.enter(badge.id(), Room.CROIX_ROUSSE, NOW)).isEqualTo(RoomEntry.refused(RefusalReason.ROOM_FULL));
      verify(presences, never()).save(argThat(RoomPresence::isOpen));
    }

    @Test
    void shouldLetRegisteredAttendeeIntoWorkshop() {
      var badge = known(badge("jane", BadgeType.ATTENDEE));
      var workshop = workshopIn(Room.FOURVIERE);
      doReturn(true).when(registrations).isRegistered(workshop.talk(), "RC-JANE");

      assertThat(venue.enter(badge.id(), Room.FOURVIERE, NOW).accepted()).isTrue();
    }

    @Test
    void shouldRefuseUnregisteredAttendeeInWorkshop() {
      var badge = known(badge("jane", BadgeType.SPONSOR));
      workshopIn(Room.FOURVIERE);

      assertThat(venue.enter(badge.id(), Room.FOURVIERE, NOW)).isEqualTo(RoomEntry.refused(RefusalReason.NOT_REGISTERED));
    }

    @Test
    void shouldLetWorkshopSpeakersAndVolunteersIn() {
      workshopIn(Room.FOURVIERE);
      var speaker = known(badge("ada", BadgeType.SPEAKER));
      var volunteer = known(ConferenceBadge.volunteer("Bob"));

      assertThat(venue.enter(speaker.id(), Room.FOURVIERE, NOW).accepted()).isTrue();
      assertThat(venue.enter(volunteer.id(), Room.FOURVIERE, NOW).accepted()).isTrue();
      verifyNoInteractions(registrations);
    }

    @Test
    void shouldNotEnterWithInactiveBadge() {
      var badge = known(badge("jane", BadgeType.ATTENDEE));
      badge.deactivate();

      assertThatThrownBy(() -> venue.enter(badge.id(), Room.SAONE, NOW)).isExactlyInstanceOf(InactiveBadgeException.class);
    }

    private RoomSession workshopIn(Room room) {
      var workshop = workshopBy("ada");
      doReturn(Optional.of(workshop)).when(sessions).sessionAt(room, NOW);

      return workshop;
    }
  }

  @Nested
  class Exit {

    @Test
    void shouldExitRoom() {
      var badge = known(badge("jane", BadgeType.ATTENDEE));
      var presence = new RoomPresence(badge.id(), Room.AMPHI, NOW.minusHours(1), null);
      doReturn(Optional.of(presence)).when(presences).openOf(badge.id());

      assertThat(venue.exit(badge.id(), NOW)).isSameAs(presence);

      assertThat(presence.isOpen()).isFalse();
      verify(presences).save(presence);
    }

    @Test
    void shouldNotExitWithoutBeingInRoom() {
      var badge = known(badge("jane", BadgeType.ATTENDEE));

      assertThatThrownBy(() -> venue.exit(badge.id(), NOW)).isExactlyInstanceOf(NotInRoomException.class);
    }
  }

  @Test
  void shouldGiveOccupancyOfEveryRoom() {
    doReturn(inside(Room.SAONE, 3)).when(presences).openIn(Room.SAONE);

    assertThat(venue.occupancy()).hasSize(5).contains(new RoomOccupancy(Room.SAONE, 3, 200), new RoomOccupancy(Room.AMPHI, 0, 500));
  }

  @Test
  void shouldRankTalksByDistinctAttendees() {
    var popular = talk("Popular");
    var quiet = talk("Quiet");
    var jane = BadgeId.newId();
    var john = BadgeId.newId();
    all.addAll(
      List.of(
        new RoomPresence(jane, Room.AMPHI, NOW, quiet),
        new RoomPresence(jane, Room.SAONE, NOW, popular),
        new RoomPresence(jane, Room.SAONE, NOW.plusMinutes(5), popular),
        new RoomPresence(john, Room.SAONE, NOW, popular),
        new RoomPresence(john, Room.RHONE, NOW, null)
      )
    );

    assertThat(venue.mostAttended()).containsExactly(new Attendance(popular.talk(), "Popular", 2), new Attendance(quiet.talk(), "Quiet", 1));
  }

  private ConferenceBadge known(ConferenceBadge badge) {
    doReturn(Optional.of(badge)).when(badges).get(badge.id());

    return badge;
  }

  private static List<RoomPresence> inside(Room room, int people) {
    return IntStream.range(0, people).mapToObj(person -> new RoomPresence(BadgeId.newId(), room, NOW, null)).toList();
  }
}

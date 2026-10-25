package fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Room;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Venue {

  private static final Comparator<Attendance> MOST_ATTENDED = Comparator.comparing(Attendance::attendees)
    .reversed()
    .thenComparing(Attendance::title);

  private final ConferenceBadgesRepository badges;
  private final RoomPresencesRepository presences;
  private final TicketsDesk tickets;
  private final RoomSessions sessions;
  private final WorkshopRegistrations registrations;

  public Venue(
    ConferenceBadgesRepository badges,
    RoomPresencesRepository presences,
    TicketsDesk tickets,
    RoomSessions sessions,
    WorkshopRegistrations registrations
  ) {
    this.badges = badges;
    this.presences = presences;
    this.tickets = tickets;
    this.sessions = sessions;
    this.registrations = registrations;
  }

  public ConferenceBadge printBadge(String ticketNumber) {
    var ticket = tickets.ticket(ticketNumber).orElseThrow(() -> new UnknownTicketForBadgeException(ticketNumber));
    if (!ticket.valid()) {
      throw new TicketNotValidForBadgeException(ticketNumber);
    }

    badges
      .ofTicket(ticket.number())
      .stream()
      .filter(ConferenceBadge::isActive)
      .forEach(previous -> {
        previous.deactivate();
        badges.save(previous);
      });

    var badge = ConferenceBadge.forTicket(ticket);
    badges.save(badge);
    return badge;
  }

  public ConferenceBadge volunteerBadge(String name) {
    var badge = ConferenceBadge.volunteer(name);

    badges.save(badge);

    return badge;
  }

  public ConferenceBadge badge(BadgeId badge) {
    return badges.badge(badge);
  }

  public RoomEntry enter(BadgeId id, Room room, LocalDateTime now) {
    var badge = badges.badge(id);
    if (!badge.isActive()) {
      throw new InactiveBadgeException(id);
    }

    presences
      .openOf(id)
      .ifPresent(previous -> {
        previous.exit(now);
        presences.save(previous);
      });

    if (presences.openIn(room).size() >= room.capacity()) {
      return RoomEntry.refused(RefusalReason.ROOM_FULL);
    }

    var session = sessions.sessionAt(room, now);
    if (session.filter(RoomSession::workshop).filter(workshop -> !mayAttend(badge, workshop)).isPresent()) {
      return RoomEntry.refused(RefusalReason.NOT_REGISTERED);
    }

    presences.save(new RoomPresence(id, room, now, session.orElse(null)));
    return RoomEntry.welcome();
  }

  public RoomPresence exit(BadgeId id, LocalDateTime now) {
    var presence = presences.openOf(badges.badge(id).id()).orElseThrow(() -> new NotInRoomException(id));

    presence.exit(now);
    presences.save(presence);

    return presence;
  }

  public List<RoomOccupancy> occupancy() {
    return Stream.of(Room.values())
      .map(room -> new RoomOccupancy(room, presences.openIn(room).size(), room.capacity()))
      .toList();
  }

  public List<Attendance> mostAttended() {
    Map<RoomSession, Long> attendees = presences
      .all()
      .stream()
      .filter(presence -> presence.session().isPresent())
      .collect(
        Collectors.groupingBy(
          presence -> presence.session().orElseThrow(),
          Collectors.collectingAndThen(Collectors.mapping(RoomPresence::badge, Collectors.toSet()), badgesInside -> (long) badgesInside.size())
        )
      );

    return attendees
      .entrySet()
      .stream()
      .map(entry -> new Attendance(entry.getKey().talk(), entry.getKey().title(), entry.getValue()))
      .sorted(MOST_ATTENDED)
      .toList();
  }

  private boolean mayAttend(ConferenceBadge badge, RoomSession workshop) {
    return (
      badge.type() == BadgeType.VOLUNTEER ||
      badge.speaksIn(workshop) ||
      badge.ticket().map(ticket -> registrations.isRegistered(workshop.talk(), ticket)).orElse(false)
    );
  }
}

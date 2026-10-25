package fr.vmz.seed4j.sample.ai.expert.conference.onsite.application;

import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.Attendance;
import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.BadgeId;
import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.ConferenceBadge;
import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.ConferenceBadgesRepository;
import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.RoomEntry;
import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.RoomOccupancy;
import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.RoomPresence;
import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.RoomPresencesRepository;
import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.RoomSessions;
import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.TicketsDesk;
import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.Venue;
import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.WorkshopRegistrations;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Room;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OnsiteApplicationService {

  private final Venue venue;
  private final Clock clock;

  public OnsiteApplicationService(
    ConferenceBadgesRepository badges,
    RoomPresencesRepository presences,
    TicketsDesk tickets,
    RoomSessions sessions,
    WorkshopRegistrations registrations,
    Clock clock
  ) {
    venue = new Venue(badges, presences, tickets, sessions, registrations);
    this.clock = clock;
  }

  public ConferenceBadge printBadge(String ticket) {
    return venue.printBadge(ticket);
  }

  public ConferenceBadge volunteerBadge(String name) {
    return venue.volunteerBadge(name);
  }

  public ConferenceBadge badge(BadgeId badge) {
    return venue.badge(badge);
  }

  public RoomEntry enter(BadgeId badge, Room room) {
    return venue.enter(badge, room, LocalDateTime.now(clock));
  }

  public RoomPresence exit(BadgeId badge) {
    return venue.exit(badge, LocalDateTime.now(clock));
  }

  public List<RoomOccupancy> occupancy() {
    return venue.occupancy();
  }

  public List<Attendance> mostAttended() {
    return venue.mostAttended();
  }
}

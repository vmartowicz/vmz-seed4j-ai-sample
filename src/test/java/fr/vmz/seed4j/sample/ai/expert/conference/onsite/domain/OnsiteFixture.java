package fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public final class OnsiteFixture {

  public static final LocalDateTime NOW = LocalDateTime.of(2026, 10, 16, 9, 55);

  private OnsiteFixture() {}

  public static TicketView ticket(String name, BadgeType type) {
    return new TicketView("RC-" + name.toUpperCase(), name, new Email(name + "@mail.com"), type, true);
  }

  public static ConferenceBadge badge(String name, BadgeType type) {
    return ConferenceBadge.forTicket(ticket(name, type));
  }

  public static RoomSession workshopBy(String speaker) {
    return new RoomSession(UUID.randomUUID(), "Hands-on hexagonal", true, List.of(new Email(speaker + "@mail.com")));
  }

  public static RoomSession talk(String title) {
    return new RoomSession(UUID.randomUUID(), title, false, List.of(new Email("speaker@mail.com")));
  }
}

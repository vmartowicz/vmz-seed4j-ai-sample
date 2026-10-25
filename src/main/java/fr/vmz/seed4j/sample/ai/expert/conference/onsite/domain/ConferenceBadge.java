package fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

public final class ConferenceBadge {

  private final BadgeId id;
  private final String name;
  private final @Nullable Email email;
  private final BadgeType type;
  private final @Nullable String ticket;
  private boolean active;

  private ConferenceBadge(String name, @Nullable Email email, BadgeType type, @Nullable String ticket) {
    Assert.field("name", name).notBlank().maxLength(100);

    id = BadgeId.newId();
    this.name = name.trim();
    this.email = email;
    this.type = type;
    this.ticket = ticket;
    active = true;
  }

  public static ConferenceBadge forTicket(TicketView ticket) {
    return new ConferenceBadge(ticket.holderName(), ticket.holderEmail(), ticket.type(), ticket.number());
  }

  public static ConferenceBadge volunteer(String name) {
    return new ConferenceBadge(name, null, BadgeType.VOLUNTEER, null);
  }

  public void deactivate() {
    active = false;
  }

  public boolean speaksIn(RoomSession session) {
    return type == BadgeType.SPEAKER && email().map(session.speakers()::contains).orElse(false);
  }

  public BadgeId id() {
    return id;
  }

  public String name() {
    return name;
  }

  public Optional<Email> email() {
    return Optional.ofNullable(email);
  }

  public BadgeType type() {
    return type;
  }

  public Optional<String> ticket() {
    return Optional.ofNullable(ticket);
  }

  public boolean isActive() {
    return active;
  }
}

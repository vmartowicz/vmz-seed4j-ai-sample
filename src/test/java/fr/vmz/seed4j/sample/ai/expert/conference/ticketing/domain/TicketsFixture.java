package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public final class TicketsFixture {

  public static final LocalDate EARLY = LocalDate.of(2026, 4, 30);
  public static final LocalDate LATE = LocalDate.of(2026, 5, 1);
  public static final LocalDateTime NOW = LocalDateTime.of(2026, 6, 1, 10, 0);

  private TicketsFixture() {}

  public static Holder jane() {
    return holder("jane");
  }

  public static Holder holder(String name) {
    return new Holder(name, new Email(name + "@mail.com"));
  }

  public static Ticket standard(Holder holder) {
    return Ticket.sold(TicketType.STANDARD, holder, new BigDecimal("349"));
  }

  public static Ticket standard() {
    return standard(jane());
  }

  public static AttendeeToRegister attendee(String name) {
    return new AttendeeToRegister(name, new Email(name + "@mail.com"), false);
  }

  public static AttendeeToRegister student(String name) {
    return new AttendeeToRegister(name, new Email(name + "@school.com"), true);
  }

  public static UUID workshop() {
    return UUID.randomUUID();
  }
}

package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

final class TicketPrices {

  private static final BigDecimal EARLY_BIRD = new BigDecimal("249");
  private static final BigDecimal STANDARD = new BigDecimal("349");
  private static final BigDecimal STUDENT = new BigDecimal("99");
  private static final LocalDate EARLY_BIRD_END = LocalDate.of(2026, 4, 30);
  private static final int EARLY_BIRD_TICKETS = 300;
  private static final int GROUP_SIZE = 5;
  private static final BigDecimal GROUP_RATE = new BigDecimal("0.90");

  private TicketPrices() {}

  static TicketType typeFor(AttendeeToRegister attendee, LocalDate today, long earlyBirdsSold) {
    if (attendee.student()) {
      return TicketType.STUDENT;
    }

    if (!today.isAfter(EARLY_BIRD_END) && earlyBirdsSold < EARLY_BIRD_TICKETS) {
      return TicketType.EARLY_BIRD;
    }

    return TicketType.STANDARD;
  }

  static BigDecimal priceOf(TicketType type, int orderSize) {
    var price = switch (type) {
      case EARLY_BIRD -> EARLY_BIRD;
      case STUDENT -> STUDENT;
      default -> STANDARD;
    };

    return orderSize >= GROUP_SIZE ? price.multiply(GROUP_RATE) : price;
  }
}

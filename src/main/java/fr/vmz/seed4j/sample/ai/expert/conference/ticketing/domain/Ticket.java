package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Edition;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public final class Ticket {

  private static final int NAME_CHANGE_DAYS = 7;
  private static final int REFUND_DAYS = 30;

  private final TicketNumber number;
  private final TicketType type;
  private final BigDecimal price;
  private Holder holder;
  private TicketStatus status;

  private Ticket(TicketType type, Holder holder, BigDecimal price) {
    number = TicketNumber.generate();
    this.type = type;
    this.holder = holder;
    this.price = price.setScale(2, RoundingMode.HALF_UP);
    status = TicketStatus.VALID;
  }

  public static Ticket sold(TicketType type, Holder holder, BigDecimal price) {
    return new Ticket(type, holder, price);
  }

  public static Ticket free(TicketType type, Holder holder) {
    return new Ticket(type, holder, BigDecimal.ZERO);
  }

  public void rename(Holder newHolder, LocalDate today) {
    assertValid();
    if (today.isAfter(Edition.FIRST_DAY.minusDays(NAME_CHANGE_DAYS))) {
      throw new NameChangeClosedException(number);
    }

    holder = newHolder;
  }

  public void refund(LocalDate today) {
    if (!type.isSold()) {
      throw new FreeTicketNotRefundableException(number);
    }

    assertValid();
    if (today.isAfter(Edition.FIRST_DAY.minusDays(REFUND_DAYS))) {
      throw new RefundPeriodOverException(number);
    }

    status = TicketStatus.REFUNDED;
  }

  public void replaceBySpeakerTicket() {
    status = TicketStatus.REFUNDED;
  }

  public void cancel() {
    status = TicketStatus.CANCELLED;
  }

  public boolean isValid() {
    return status == TicketStatus.VALID;
  }

  public boolean isHeldBy(Holder other) {
    return holder.email().equals(other.email());
  }

  private void assertValid() {
    if (!isValid()) {
      throw new TicketNotValidException(number, status);
    }
  }

  public TicketNumber number() {
    return number;
  }

  public TicketType type() {
    return type;
  }

  public Holder holder() {
    return holder;
  }

  public BigDecimal price() {
    return price;
  }

  public TicketStatus status() {
    return status;
  }
}

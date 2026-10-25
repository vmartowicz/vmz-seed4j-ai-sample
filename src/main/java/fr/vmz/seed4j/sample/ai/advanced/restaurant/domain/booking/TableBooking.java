package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner.Contact;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.table.DiningTable;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.table.PartySize;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

public class TableBooking {

  private static final int FREE_CANCELLATION_HOURS = 48;

  private final TableBookingId id;
  private final Contact contact;
  private final Slot slot;
  private final PartySize party;
  private final @Nullable String comments;
  private final List<DiningTable> tables;
  private BookingStatus status;
  private Optional<Deposit> deposit;

  TableBooking(TableBookingId id, BookingToCreate booking, List<DiningTable> tables, Optional<Deposit> deposit) {
    Assert.notNull("id", id);
    Assert.field("tables", tables).notEmpty();

    this.id = id;
    contact = booking.contact();
    slot = booking.slot();
    party = booking.party();
    comments = booking.comments();
    this.tables = List.copyOf(tables);
    this.deposit = deposit;
    status = deposit.isPresent() ? BookingStatus.PENDING : BookingStatus.CONFIRMED;
  }

  public void payDeposit(BigDecimal amount, LocalDateTime now) {
    Assert.notNull("amount", amount);
    assertStatus(BookingStatus.PENDING, "pay the deposit of");

    var expected = deposit.orElseThrow();
    if (!expected.accepts(amount)) {
      throw new WrongDepositAmountException(expected.amount(), amount);
    }

    if (now.isAfter(expected.dueBy())) {
      throw new DepositDeadlinePassedException(id, expected.dueBy());
    }

    deposit = Optional.of(expected.paid());
    status = BookingStatus.CONFIRMED;
  }

  public void cancel(LocalDateTime now) {
    if (!status.isOpen()) {
      throw new InvalidBookingTransitionException(id, status, "cancel");
    }

    var freeCancellation = now.isBefore(slot.at().minusHours(FREE_CANCELLATION_HOURS));
    deposit = deposit.map(current -> settle(current, freeCancellation));
    status = BookingStatus.CANCELLED;
  }

  public boolean expireIfUnpaid(LocalDateTime now) {
    if (status != BookingStatus.PENDING || !now.isAfter(deposit.orElseThrow().dueBy())) {
      return false;
    }

    status = BookingStatus.CANCELLED;
    return true;
  }

  public void honor(LocalDateTime now) {
    assertStatus(BookingStatus.CONFIRMED, "check in");
    assertBookingDay(now);

    status = BookingStatus.HONORED;
  }

  public void markNoShow(LocalDateTime now) {
    assertStatus(BookingStatus.CONFIRMED, "mark as no-show");
    assertBookingDay(now);
    if (now.isBefore(slot.at())) {
      throw new NoShowTooEarlyException(id, slot.at());
    }

    deposit = deposit.map(Deposit::forfeited);
    status = BookingStatus.NO_SHOW;
  }

  public boolean holdsTablesFor(LocalDate date, MealService service) {
    return status.holdsTables() && slot.date().equals(date) && slot.service() == service;
  }

  private static Deposit settle(Deposit deposit, boolean freeCancellation) {
    if (!deposit.isPaid()) {
      return deposit;
    }

    return freeCancellation ? deposit.refunded() : deposit.forfeited();
  }

  private void assertStatus(BookingStatus expected, String action) {
    if (status != expected) {
      throw new InvalidBookingTransitionException(id, status, action);
    }
  }

  private void assertBookingDay(LocalDateTime now) {
    if (!now.toLocalDate().equals(slot.date())) {
      throw new NotBookingDayException(id, slot.date());
    }
  }

  public TableBookingId id() {
    return id;
  }

  public Contact contact() {
    return contact;
  }

  public Slot slot() {
    return slot;
  }

  public PartySize party() {
    return party;
  }

  public Optional<String> comments() {
    return Optional.ofNullable(comments);
  }

  public List<DiningTable> tables() {
    return tables;
  }

  public BookingStatus status() {
    return status;
  }

  public Optional<Deposit> deposit() {
    return deposit;
  }
}

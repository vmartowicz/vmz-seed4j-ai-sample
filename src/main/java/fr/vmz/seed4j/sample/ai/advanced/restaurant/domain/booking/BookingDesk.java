package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner.Diner;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner.DinerEmail;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner.DinersRepository;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.table.DiningRoom;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.table.DiningTable;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.table.PartySize;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public class BookingDesk {

  private static final Comparator<TableBooking> BY_ARRIVAL = Comparator.comparing((TableBooking booking) -> booking.slot().arrival()).thenComparing(
    booking -> booking.contact().fullName()
  );

  private final DinersRepository diners;
  private final TableBookingsRepository bookings;

  public BookingDesk(DinersRepository diners, TableBookingsRepository bookings) {
    this.diners = diners;
    this.bookings = bookings;
  }

  public TableBooking book(BookingToCreate booking, LocalDateTime now) {
    Assert.notNull("booking", booking);

    var email = booking.contact().email();
    var diner = diners.get(email).orElseGet(() -> Diner.newcomer(email));
    diner.assertCanBook(now.toLocalDate());

    var slot = booking.slot();
    slot.assertBookableFrom(now);

    var tables = freeTables(slot.date(), slot.service(), booking.party(), diner.isLoyal());
    if (tables.isEmpty()) {
      throw new NoTableAvailableException(slot, booking.party());
    }

    var created = booking.create(tables, diner.isLoyal(), now);
    diners.save(diner);
    bookings.save(created);

    return created;
  }

  public TableBooking payDeposit(TableBookingId id, BigDecimal amount, LocalDateTime now) {
    var booking = bookings.booking(id);

    booking.payDeposit(amount, now);
    bookings.save(booking);

    return booking;
  }

  public TableBooking cancel(TableBookingId id, LocalDateTime now) {
    var booking = bookings.booking(id);
    var diner = diners.diner(booking.contact().email());

    booking.cancel(now);
    diner.cancel(now.toLocalDate());
    bookings.save(booking);
    diners.save(diner);

    return booking;
  }

  public TableBooking checkIn(TableBookingId id, LocalDateTime now) {
    var booking = bookings.booking(id);
    var diner = diners.diner(booking.contact().email());

    booking.honor(now);
    diner.honor();
    bookings.save(booking);
    diners.save(diner);

    return booking;
  }

  public TableBooking noShow(TableBookingId id, LocalDateTime now) {
    var booking = bookings.booking(id);
    var diner = diners.diner(booking.contact().email());

    booking.markNoShow(now);
    diner.noShow();
    bookings.save(booking);
    diners.save(diner);

    return booking;
  }

  public void expireUnpaid(LocalDateTime now) {
    bookings
      .pending()
      .stream()
      .filter(booking -> booking.expireIfUnpaid(now))
      .forEach(bookings::save);
  }

  public List<AvailableSlot> availability(LocalDate date, MealService service, PartySize party, LocalDateTime now) {
    var tables = freeTables(date, service, party, false);
    if (tables.isEmpty()) {
      return List.of();
    }

    return service
      .arrivalTimes()
      .stream()
      .map(arrival -> new Slot(date, arrival))
      .filter(slot -> slot.isBookableFrom(now))
      .map(slot -> new AvailableSlot(slot.arrival(), tables))
      .toList();
  }

  public List<TableBooking> dayBookings(LocalDate date, MealService service) {
    return bookings
      .on(date)
      .stream()
      .filter(booking -> booking.slot().service() == service)
      .filter(booking -> booking.status() != BookingStatus.CANCELLED)
      .sorted(BY_ARRIVAL)
      .toList();
  }

  public Diner diner(DinerEmail email) {
    return diners.diner(email);
  }

  private List<DiningTable> freeTables(LocalDate date, MealService service, PartySize party, boolean loyal) {
    var occupied = bookings
      .on(date)
      .stream()
      .filter(booking -> booking.holdsTablesFor(date, service))
      .flatMap(booking -> booking.tables().stream())
      .toList();

    return DiningRoom.seat(party, occupied, DiningRoom.preferredPosition(date, party, loyal));
  }
}

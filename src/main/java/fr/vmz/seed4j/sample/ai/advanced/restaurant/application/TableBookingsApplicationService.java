package fr.vmz.seed4j.sample.ai.advanced.restaurant.application;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking.AvailableSlot;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking.BookingDesk;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking.BookingToCreate;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking.MealService;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking.TableBooking;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking.TableBookingId;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking.TableBookingsRepository;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner.DinersRepository;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.table.PartySize;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TableBookingsApplicationService {

  private final TableBookingsRepository bookings;
  private final BookingDesk desk;
  private final Clock clock;

  public TableBookingsApplicationService(DinersRepository diners, TableBookingsRepository bookings, Clock clock) {
    this.bookings = bookings;
    desk = new BookingDesk(diners, bookings);
    this.clock = clock;
  }

  public TableBooking book(BookingToCreate booking) {
    return desk.book(booking, now());
  }

  public TableBooking booking(TableBookingId booking) {
    return bookings.booking(booking);
  }

  public TableBooking payDeposit(TableBookingId booking, BigDecimal amount) {
    return desk.payDeposit(booking, amount, now());
  }

  public TableBooking cancel(TableBookingId booking) {
    return desk.cancel(booking, now());
  }

  public TableBooking checkIn(TableBookingId booking) {
    return desk.checkIn(booking, now());
  }

  public TableBooking noShow(TableBookingId booking) {
    return desk.noShow(booking, now());
  }

  public void expireUnpaid() {
    desk.expireUnpaid(now());
  }

  public List<AvailableSlot> availability(LocalDate date, MealService service, PartySize party) {
    return desk.availability(date, service, party, now());
  }

  public List<TableBooking> dayBookings(LocalDate date, MealService service) {
    return desk.dayBookings(date, service);
  }

  private LocalDateTime now() {
    return LocalDateTime.now(clock);
  }
}

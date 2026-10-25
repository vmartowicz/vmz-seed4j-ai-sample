package fr.vmz.seed4j.sample.ai.advanced.restaurant.infrastructure.primary;

import static fr.vmz.seed4j.sample.ai.advanced.restaurant.infrastructure.primary.RestaurantApi.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.vmz.seed4j.sample.ai.IntegrationTest;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTest
@AutoConfigureMockMvc
@Import(MovableClockConfiguration.class)
class TableBookingsResourceIT {

  @Autowired
  private MockMvc rest;

  @Autowired
  private MovableClock clock;

  @Autowired
  private UnpaidDepositsJob unpaidDeposits;

  private RestaurantApi api;
  private LocalDate day;
  private String email;

  @BeforeEach
  void loadApi() {
    api = new RestaurantApi(rest);
    email = uniqueEmail();
    bookingDay(freeTuesday());
  }

  @Test
  void shouldBookCoupleAtWindowTable() throws Exception {
    api
      .book(email.toUpperCase(), day, "19:30", 2)
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id").isNotEmpty())
      .andExpect(jsonPath("$.date").value(day.toString()))
      .andExpect(jsonPath("$.time").value(startsWith("19:30")))
      .andExpect(jsonPath("$.service").value("DINNER"))
      .andExpect(jsonPath("$.people").value(2))
      .andExpect(jsonPath("$.fullName").value("Jane Doe"))
      .andExpect(jsonPath("$.email").value(email))
      .andExpect(jsonPath("$.phone").value("06 12 34 56 78"))
      .andExpect(jsonPath("$.comments").value("Birthday"))
      .andExpect(jsonPath("$.status").value("CONFIRMED"))
      .andExpect(jsonPath("$.tables[0].number").value(1))
      .andExpect(jsonPath("$.tables[0].seats").value(2))
      .andExpect(jsonPath("$.tables[0].position").value("WINDOW"))
      .andExpect(jsonPath("$.deposit").doesNotExist());
  }

  @Test
  void shouldPreferTerraceInSummer() throws Exception {
    bookingDay(freeSummerTuesday());

    api.book(email, day, "12:00", 2).andExpect(status().isCreated()).andExpect(jsonPath("$.tables[0].position").value("TERRACE"));
  }

  @Test
  void shouldCombineTwoTablesFor4For8People() throws Exception {
    api
      .book(email, day, "20:00", 8)
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.tables[*].number").value(contains(15, 16)));
  }

  @Test
  void shouldConfirmBookingOf6PeopleOnceDepositIsPaid() throws Exception {
    var booking = api.book(email, day, "19:30", 6);
    booking
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.status").value("PENDING"))
      .andExpect(jsonPath("$.deposit.amount").value(120))
      .andExpect(jsonPath("$.deposit.status").value("EXPECTED"))
      .andExpect(jsonPath("$.deposit.dueBy").value(startsWith(day.minusDays(1) + "T10:00")));
    var id = api.bookingId(email, day, "21:00", 6);

    api
      .payDeposit(id, "120")
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status").value("CONFIRMED"))
      .andExpect(jsonPath("$.deposit.status").value("PAID"));
  }

  @Test
  void shouldNotAcceptWrongDepositAmount() throws Exception {
    var id = api.bookingId(email, day, "19:30", 6);

    api.payDeposit(id, "100").andExpect(status().isConflict()).andExpect(jsonPath("$.detail").value(containsString("120")));
  }

  @Test
  void shouldNotBookWhenRestaurantIsClosed() throws Exception {
    api
      .book(email, day.plusDays(5), "19:30", 2)
      .andExpect(status().isConflict())
      .andExpect(jsonPath("$.title").value("Business rule violation"))
      .andExpect(jsonPath("$.detail").value(containsString("closed")));
  }

  @Test
  void shouldNotBookMoreThan30DaysAhead() throws Exception {
    api.book(email, day.plusWeeks(5), "19:30", 2).andExpect(status().isConflict());
  }

  @Test
  void shouldNotBookLessThan2HoursBeforeService() throws Exception {
    clock.at(day.atTime(17, 1));

    api.book(email, day, "21:00", 2).andExpect(status().isConflict()).andExpect(jsonPath("$.detail").value(containsString("2 hours")));
  }

  @Test
  void shouldNotBookWithoutFreeTable() throws Exception {
    api.bookingId(email, day, "19:00", 5);
    api.bookingId(uniqueEmail(), day, "21:30", 6);

    api.book(uniqueEmail(), day, "20:00", 5).andExpect(status().isConflict());
  }

  @Test
  void shouldNotBookWithInvalidPhoneNumber() throws Exception {
    api
      .postJson("/api/restaurant/bookings", body("\"phone\":\"+33612345678\",\"time\":\"19:30\",\"people\":2"))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.title").value("Invalid value"));
  }

  @Test
  void shouldNotBookOutsideServiceSlots() throws Exception {
    api.book(email, day, "14:00", 2).andExpect(status().isBadRequest()).andExpect(jsonPath("$.detail").value(containsString("14:00")));
  }

  @Test
  void shouldNotBookMoreThan8People() throws Exception {
    api.book(email, day, "19:30", 9).andExpect(status().isBadRequest()).andExpect(jsonPath("$.detail").value(containsString("people")));
  }

  @Test
  void shouldNotBookWithoutName() throws Exception {
    api
      .postJson("/api/restaurant/bookings", "{\"phone\":\"0612345678\",\"email\":\"%s\",\"date\":\"%s\",\"time\":\"19:30\",\"people\":2}".formatted(email, day))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.detail").value(containsString("fullName")));
  }

  @Test
  void shouldRefundDepositWhenCancellingMoreThan48HoursBefore() throws Exception {
    var id = paidBookingOf6();

    api
      .step(id, "cancellation")
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status").value("CANCELLED"))
      .andExpect(jsonPath("$.deposit.status").value("REFUNDED"));
  }

  @Test
  void shouldForfeitDepositWhenCancellingLessThan48HoursBefore() throws Exception {
    var id = paidBookingOf6();
    clock.at(day.atTime(10, 0));

    api.step(id, "cancellation").andExpect(status().isOk()).andExpect(jsonPath("$.deposit.status").value("FORFEITED"));
  }

  @Test
  void shouldNotCancelTwice() throws Exception {
    var id = api.bookingId(email, day, "19:30", 2);
    api.step(id, "cancellation").andExpect(status().isOk());

    api.step(id, "cancellation").andExpect(status().isConflict());
  }

  @Test
  void shouldCheckInOnBookingDay() throws Exception {
    var id = api.bookingId(email, day, "19:30", 2);
    clock.at(day.atTime(19, 25));

    api.step(id, "check-in").andExpect(status().isOk()).andExpect(jsonPath("$.status").value("HONORED"));
  }

  @Test
  void shouldNotCheckInBeforeBookingDay() throws Exception {
    var id = api.bookingId(email, day, "19:30", 2);

    api.step(id, "check-in").andExpect(status().isConflict());
  }

  @Test
  void shouldForfeitDepositOfNoShow() throws Exception {
    var id = paidBookingOf6();
    clock.at(day.atTime(21, 0));

    api
      .step(id, "no-show")
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status").value("NO_SHOW"))
      .andExpect(jsonPath("$.deposit.status").value("FORFEITED"));
  }

  @Test
  void shouldNotMarkNoShowBeforeArrivalTime() throws Exception {
    var id = api.bookingId(email, day, "19:30", 2);
    clock.at(day.atTime(19, 0));

    api.step(id, "no-show").andExpect(status().isConflict());
  }

  @Test
  void shouldCancelUnpaidBookingsAndFreeTheirTables() throws Exception {
    var first = api.bookingId(email, day, "19:00", 6);
    var second = api.bookingId(uniqueEmail(), day, "19:00", 6);
    api.payDeposit(second, "120").andExpect(status().isOk());
    clock.at(day.minusDays(1).atTime(10, 1));

    unpaidDeposits.cancelUnpaidBookings();

    api.get("/api/restaurant/bookings/" + first).andExpect(jsonPath("$.status").value("CANCELLED"));
    api.get("/api/restaurant/bookings/" + second).andExpect(jsonPath("$.status").value("CONFIRMED"));
    api.book(uniqueEmail(), day, "20:00", 6).andExpect(status().isCreated()).andExpect(jsonPath("$.tables[0].number").value(18));
  }

  @Test
  void shouldNotViewUnknownBooking() throws Exception {
    api
      .get("/api/restaurant/bookings/" + UUID.randomUUID())
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.title").value("Unknown element"));
  }

  @Test
  void shouldListAvailableArrivalTimesWithTables() throws Exception {
    api.bookingId(email, day, "19:00", 2);

    api
      .get("/api/restaurant/availability?date=%s&service=DINNER&people=2".formatted(day))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()").value(6))
      .andExpect(jsonPath("$[0].time").value(startsWith("19:00")))
      .andExpect(jsonPath("$[0].tables[0].number").value(2));
  }

  @Test
  void shouldListBookingsOfService() throws Exception {
    var late = api.bookingId(email, day, "20:30", 2);
    var early = api.bookingId(uniqueEmail(), day, "19:00", 4);
    api.bookingId(uniqueEmail(), day, "12:00", 2);

    api
      .get("/api/restaurant/bookings?date=%s&service=DINNER".formatted(day))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[*].id").value(contains(early, late)));
  }

  private String paidBookingOf6() throws Exception {
    var id = api.bookingId(email, day, "19:30", 6);
    api.payDeposit(id, "120").andExpect(status().isOk());

    return id;
  }

  private String body(String fields) {
    return "{\"fullName\":\"Jane Doe\",\"email\":\"%s\",\"date\":\"%s\",%s}".formatted(email, day, fields);
  }

  private void bookingDay(LocalDate tuesday) {
    day = tuesday;
    clock.at(day.minusDays(3).atTime(10, 0));
  }
}

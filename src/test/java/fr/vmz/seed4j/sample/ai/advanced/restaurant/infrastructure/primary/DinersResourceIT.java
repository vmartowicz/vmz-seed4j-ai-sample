package fr.vmz.seed4j.sample.ai.advanced.restaurant.infrastructure.primary;

import static fr.vmz.seed4j.sample.ai.advanced.restaurant.infrastructure.primary.RestaurantApi.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.vmz.seed4j.sample.ai.IntegrationTest;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTest
@AutoConfigureMockMvc
@Import(MovableClockConfiguration.class)
class DinersResourceIT {

  @Autowired
  private MockMvc rest;

  @Autowired
  private MovableClock clock;

  private RestaurantApi api;
  private String email;

  @BeforeEach
  void loadApi() {
    api = new RestaurantApi(rest);
    email = uniqueEmail();
  }

  @Test
  void shouldViewNewDinerAsRegular() throws Exception {
    api.bookingId(email, bookingDay(), "19:30", 2);

    api
      .get("/api/restaurant/diners/" + email)
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.email").value(email))
      .andExpect(jsonPath("$.status").value("REGULAR"))
      .andExpect(jsonPath("$.honoredBookings").value(0))
      .andExpect(jsonPath("$.noShows").value(0))
      .andExpect(jsonPath("$.cancellationsThisYear").value(0));
  }

  @Test
  void shouldNotViewUnknownDiner() throws Exception {
    api.get("/api/restaurant/diners/" + email).andExpect(status().isNotFound()).andExpect(jsonPath("$.title").value("Unknown element"));
  }

  @Test
  void shouldBlacklistDinerAfter3NoShows() throws Exception {
    for (int visit = 0; visit < 3; visit++) {
      var day = bookingDay();
      var id = api.bookingId(email, day, "19:30", 2);
      clock.at(day.atTime(22, 0));
      api.step(id, "no-show").andExpect(status().isOk());
    }

    api.book(email, bookingDay(), "19:30", 2).andExpect(status().isConflict()).andExpect(jsonPath("$.detail").value(containsString("blacklisted")));
    api.get("/api/restaurant/diners/" + email).andExpect(jsonPath("$.status").value("BLACKLISTED")).andExpect(jsonPath("$.noShows").value(3));
  }

  @Test
  void shouldWelcomeLoyalDinerWithoutDepositAfter5HonoredBookings() throws Exception {
    for (int visit = 0; visit < 5; visit++) {
      var day = bookingDay();
      var id = api.bookingId(email, day, "19:30", 2);
      clock.at(day.atTime(19, 30));
      api.step(id, "check-in").andExpect(status().isOk());
    }

    api
      .book(email, bookingDay(), "19:30", 6)
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.status").value("CONFIRMED"))
      .andExpect(jsonPath("$.deposit").doesNotExist());
    api.get("/api/restaurant/diners/" + email).andExpect(jsonPath("$.status").value("LOYAL")).andExpect(jsonPath("$.honoredBookings").value(5));
  }

  @Test
  void shouldNotBookAfter3CancellationsThisYear() throws Exception {
    var day = bookingDay();
    for (var time : new String[] { "19:00", "19:30", "20:00" }) {
      api.step(api.bookingId(email, day, time, 2), "cancellation").andExpect(status().isOk());
    }

    api.book(email, day, "20:30", 2).andExpect(status().isConflict()).andExpect(jsonPath("$.detail").value(containsString("3 bookings")));
    api.get("/api/restaurant/diners/" + email).andExpect(jsonPath("$.cancellationsThisYear").value(3));
  }

  private LocalDate bookingDay() {
    var day = freeTuesday();
    clock.at(day.minusDays(3).atTime(10, 0));

    return day;
  }
}

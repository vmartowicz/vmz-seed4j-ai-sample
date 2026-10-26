package fr.vmz.seed4j.sample.ai.expert.rental.reservation.infrastructure.primary;

import static fr.vmz.seed4j.sample.ai.expert.rental.RentalApi.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.vmz.seed4j.sample.ai.IntegrationTest;
import fr.vmz.seed4j.sample.ai.expert.rental.RentalApi;
import fr.vmz.seed4j.sample.ai.expert.rental.RentalClock;
import fr.vmz.seed4j.sample.ai.expert.rental.RentalClockConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import java.util.UUID;

@IntegrationTest
@AutoConfigureMockMvc
@Import(RentalClockConfiguration.class)
class RentalReservationsResourceIT {

  @Autowired
  private MockMvc rest;

  @Autowired
  private RentalClock clock;

  private RentalApi api;

  @BeforeEach
  void loadApi() {
    api = new RentalApi(rest);
    clock.at(NOW);
  }

  @Test
  void shouldBookAndPayCategory() throws Exception {
    var email = uniqueEmail();

    api
      .book(email, "1990-05-04", "SUV", PICK_UP, 3, "SAINT_EXUPERY", ",\"childSeat\":true,\"gps\":true")
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id").isNotEmpty())
      .andExpect(jsonPath("$.driverName").value("Jane Doe"))
      .andExpect(jsonPath("$.driverEmail").value(email))
      .andExpect(jsonPath("$.category").value("SUV"))
      .andExpect(jsonPath("$.pickUpPoint").value("PART_DIEU"))
      .andExpect(jsonPath("$.returnPoint").value("SAINT_EXUPERY"))
      .andExpect(jsonPath("$.days").value(3))
      .andExpect(jsonPath("$.dailyRate").value(69))
      .andExpect(jsonPath("$.options").value(39))
      .andExpect(jsonPath("$.total").value(246))
      .andExpect(jsonPath("$.status").value("CONFIRMED"));
  }

  @Test
  void shouldChargeYoungDrivers() throws Exception {
    api.book(uniqueEmail(), "2003-01-01", "ECO", PICK_UP, 2, "PART_DIEU", "").andExpect(jsonPath("$.youngDriverFee").value(30)).andExpect(jsonPath("$.total").value(100));
  }

  @Test
  void shouldNotRentPremiumToYoungDrivers() throws Exception {
    api.book(uniqueEmail(), "2003-01-01", "PREMIUM", PICK_UP, 2, "PART_DIEU", "").andExpect(status().isConflict()).andExpect(jsonPath("$.title").value("Business rule violation"));
  }

  @Test
  void shouldNotRentToDriversUnder21() throws Exception {
    api.book(uniqueEmail(), "2006-01-01", "ECO", PICK_UP, 2, "PART_DIEU", "").andExpect(status().isConflict()).andExpect(jsonPath("$.detail").value(containsString("21")));
  }

  @Test
  void shouldNotOfferFreeDayWithoutPoints() throws Exception {
    api.book(uniqueEmail(), "1990-05-04", "ECO", PICK_UP, 2, "PART_DIEU", ",\"freeDay\":true").andExpect(status().isConflict()).andExpect(jsonPath("$.detail").value(containsString("500")));
  }

  @Test
  void shouldRefundEverythingWhenCancellingEarly() throws Exception {
    var reservation = api.reservation(uniqueEmail(), "ECO", 3);

    cancel(reservation).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("CANCELLED")).andExpect(jsonPath("$.refund").value(105));
  }

  @Test
  void shouldKeepOneDayWhenCancellingLate() throws Exception {
    var reservation = api.reservation(uniqueEmail(), "ECO", 3);
    clock.at(PICK_UP.minusHours(12));

    cancel(reservation).andExpect(jsonPath("$.refund").value(70));
    api.get("/api/rental/reservations/" + reservation).andExpect(jsonPath("$.status").value("CANCELLED"));
    cancel(reservation).andExpect(status().isConflict());
  }

  @Test
  void shouldNotBookReturnBeforePickUp() throws Exception {
    api.book(uniqueEmail(), "1990-05-04", "ECO", PICK_UP, 0, "PART_DIEU", "").andExpect(status().isBadRequest()).andExpect(jsonPath("$.title").value("Invalid value"));
  }

  @Test
  void shouldNotBookWithInvalidEmail() throws Exception {
    api.book("jane@mail", "ECO", 2).andExpect(status().isBadRequest()).andExpect(jsonPath("$.detail").value(containsString("jane@mail")));
  }

  @Test
  void shouldNotBookWithoutCategory() throws Exception {
    api
      .postJson("/api/rental/reservations", "{\"driver\":{\"name\":\"Jane\",\"email\":\"jane@mail.com\",\"birthDate\":\"1990-01-01\",\"licenceDate\":\"2010-01-01\"}}")
      .andExpect(status().isBadRequest());
  }

  @Test
  void shouldNotFindUnknownReservation() throws Exception {
    api.get("/api/rental/reservations/" + UUID.randomUUID()).andExpect(status().isNotFound()).andExpect(jsonPath("$.title").value("Unknown element"));
  }

  private ResultActions cancel(String reservation) throws Exception {
    return api.postJson("/api/rental/reservations/" + reservation + "/cancellation", "{}");
  }
}

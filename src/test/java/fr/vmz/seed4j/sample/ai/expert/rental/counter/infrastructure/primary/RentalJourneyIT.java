package fr.vmz.seed4j.sample.ai.expert.rental.counter.infrastructure.primary;

import static fr.vmz.seed4j.sample.ai.expert.rental.RentalApi.*;
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

@IntegrationTest
@AutoConfigureMockMvc
@Import(RentalClockConfiguration.class)
class RentalJourneyIT {

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
  void shouldSpendPointsEarnedOnPreviousRentalForAFreeDay() throws Exception {
    var email = uniqueEmail();
    api.vehicle("PREMIUM", "PART_DIEU");
    var premium = api.reservation(email, "PREMIUM", 5);
    clock.at(PICK_UP);
    var contract = api.contract(premium);
    clock.at(PICK_UP.plusDays(5));
    api.giveBack(contract, "PART_DIEU", 2000, "0", null).andExpect(status().isOk());
    api.get("/api/rental/loyalty/" + email).andExpect(jsonPath("$.balance").value(595)).andExpect(jsonPath("$.status").value("MEMBER"));

    var freeDay = read(
      api.book(email, "1990-05-04", "ECO", PICK_UP.plusDays(20), 3, "PART_DIEU", ",\"freeDay\":true").andExpect(status().isCreated()).andExpect(jsonPath("$.freeDay").value(35)).andExpect(jsonPath("$.total").value(70)),
      "$.id"
    );
    api.get("/api/rental/loyalty/" + email).andExpect(jsonPath("$.balance").value(95));

    api.postJson("/api/rental/reservations/" + freeDay + "/cancellation", "{}").andExpect(status().isOk());

    api.get("/api/rental/loyalty/" + email).andExpect(jsonPath("$.balance").value(595));
  }
}

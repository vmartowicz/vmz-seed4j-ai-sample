package fr.vmz.seed4j.sample.ai.expert.rental.counter.infrastructure.primary;

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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@IntegrationTest
@AutoConfigureMockMvc
@Import(RentalClockConfiguration.class)
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
class CounterUpgradesIT {

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
  @DirtiesContext
  void shouldUpgradeForFreeWhenBookedCategoryIsMissing() throws Exception {
    api.vehicle("SUV", "PART_DIEU");
    api.vehicle("ECO", "VILLEURBANNE");
    var reservation = api.reservation(uniqueEmail(), "ECO", 3);
    clock.at(PICK_UP);

    api.pickUp(reservation).andExpect(status().isCreated()).andExpect(jsonPath("$.vehicleCategory").value("SUV")).andExpect(jsonPath("$.deposit").value(800));
  }

  @Test
  @DirtiesContext
  void shouldNotUpgradeYoungDriversToPremium() throws Exception {
    api.vehicle("PREMIUM", "PART_DIEU");
    var reservation = read(api.book(uniqueEmail(), "2003-01-01", "SUV", PICK_UP, 2, "PART_DIEU", "").andExpect(status().isCreated()), "$.id");
    clock.at(PICK_UP);

    api.pickUp(reservation).andExpect(status().isConflict()).andExpect(jsonPath("$.detail").value(containsString("SUV")));
    api.get("/api/rental/reservations/" + reservation).andExpect(jsonPath("$.status").value("CONFIRMED"));
  }
}

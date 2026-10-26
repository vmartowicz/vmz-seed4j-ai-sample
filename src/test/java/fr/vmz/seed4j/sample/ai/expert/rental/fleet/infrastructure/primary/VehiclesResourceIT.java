package fr.vmz.seed4j.sample.ai.expert.rental.fleet.infrastructure.primary;

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

@IntegrationTest
@AutoConfigureMockMvc
@Import(RentalClockConfiguration.class)
class VehiclesResourceIT {

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
  void shouldRegisterAvailableVehicle() throws Exception {
    var plate = freePlate();

    api
      .registerVehicle(plate.toLowerCase(), "COMFORT", "ELECTRIC", "VILLEURBANNE")
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.plate").value(plate))
      .andExpect(jsonPath("$.make").value("Renault"))
      .andExpect(jsonPath("$.model").value("Clio"))
      .andExpect(jsonPath("$.category").value("COMFORT"))
      .andExpect(jsonPath("$.fuel").value("ELECTRIC"))
      .andExpect(jsonPath("$.mileage").value(20000))
      .andExpect(jsonPath("$.point").value("VILLEURBANNE"))
      .andExpect(jsonPath("$.status").value("AVAILABLE"))
      .andExpect(jsonPath("$.nextServiceOn").value("2027-05-01"))
      .andExpect(jsonPath("$.nextServiceMileage").value(35000));
    api.get("/api/rental/fleet/vehicles?point=VILLEURBANNE&category=COMFORT").andExpect(jsonPath("$[*].plate").value(hasItem(plate)));
  }

  @Test
  void shouldNotRegisterSamePlateTwice() throws Exception {
    var plate = api.vehicle("ECO", "PART_DIEU");

    api.registerVehicle(plate, "ECO", "PETROL", "PART_DIEU").andExpect(status().isConflict()).andExpect(jsonPath("$.title").value("Business rule violation"));
  }

  @Test
  void shouldNotRegisterInvalidPlate() throws Exception {
    api.registerVehicle("AB123CD", "ECO", "PETROL", "PART_DIEU").andExpect(status().isBadRequest()).andExpect(jsonPath("$.title").value("Invalid value"));
  }

  @Test
  void shouldNotRegisterWithoutModel() throws Exception {
    api
      .postJson("/api/rental/fleet/vehicles", "{\"plate\":\"" + freePlate() + "\",\"make\":\"Renault\",\"category\":\"ECO\",\"fuel\":\"PETROL\",\"mileage\":1,\"firstRegistration\":\"2025-01-10\",\"lastService\":\"2026-05-01\",\"point\":\"PART_DIEU\"}")
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.detail").value(containsString("model")));
  }

  @Test
  void shouldTransferAndServiceVehicle() throws Exception {
    var plate = api.vehicle("SUV", "PART_DIEU");

    api.postJson("/api/rental/fleet/vehicles/" + plate + "/transfer", "{\"point\":\"SAINT_EXUPERY\"}").andExpect(status().isOk()).andExpect(jsonPath("$.point").value("SAINT_EXUPERY"));
    clock.at(NOW.plusDays(10));
    api
      .postJson("/api/rental/fleet/vehicles/" + plate + "/maintenance-completion", "{}")
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status").value("AVAILABLE"))
      .andExpect(jsonPath("$.nextServiceOn").value("2027-06-11"));
    api.get("/api/rental/fleet/vehicles/" + plate).andExpect(status().isOk()).andExpect(jsonPath("$.plate").value(plate));
  }

  @Test
  void shouldNotFindUnknownVehicle() throws Exception {
    api.get("/api/rental/fleet/vehicles/ZZ-999-ZZ").andExpect(status().isNotFound()).andExpect(jsonPath("$.title").value("Unknown element"));
  }
}

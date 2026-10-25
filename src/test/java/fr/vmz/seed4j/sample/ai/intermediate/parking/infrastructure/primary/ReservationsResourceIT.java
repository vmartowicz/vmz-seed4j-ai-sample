package fr.vmz.seed4j.sample.ai.intermediate.parking.infrastructure.primary;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jayway.jsonpath.JsonPath;
import fr.vmz.seed4j.sample.ai.IntegrationTest;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@IntegrationTest
@AutoConfigureMockMvc
@Import(FixedClockConfiguration.class)
class ReservationsResourceIT {

  // In-memory reservations are shared by the whole Spring context: each test uses its own resident, spaces and dates
  @Autowired
  private MockMvc rest;

  @Test
  void shouldReserveSpace() throws Exception {
    var resident = uniqueResident();

    reserve(resident, 1, "2026-03-12")
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id").isNotEmpty())
      .andExpect(jsonPath("$.residentName").value(resident))
      .andExpect(jsonPath("$.spaceNumber").value(1))
      .andExpect(jsonPath("$.date").value("2026-03-12"))
      .andExpect(jsonPath("$.status").value("ACTIVE"));
  }

  @Test
  void shouldNotReserveSpaceAlreadyReserved() throws Exception {
    reserve(uniqueResident(), 2, "2026-03-12").andExpect(status().isCreated());

    reserve(uniqueResident(), 2, "2026-03-12")
      .andExpect(status().isConflict())
      .andExpect(jsonPath("$.title").value("Business rule violation"));
  }

  @Test
  void shouldNotReserveMoreThan7DaysInAdvance() throws Exception {
    reserve(uniqueResident(), 1, "2026-03-18").andExpect(status().isConflict());
  }

  @Test
  void shouldNotReserveMoreThan2SpacesPerMonth() throws Exception {
    var resident = uniqueResident();
    reserve(resident, 3, "2026-03-12").andExpect(status().isCreated());
    reserve(resident, 3, "2026-03-13").andExpect(status().isCreated());

    reserve(resident, 3, "2026-03-14").andExpect(status().isConflict());
  }

  @Test
  void shouldNotReserveUnknownSpace() throws Exception {
    reserve(uniqueResident(), 11, "2026-03-12")
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.title").value("Invalid value"));
  }

  @Test
  void shouldCancelReservation() throws Exception {
    var reservationId = idOf(reserve(uniqueResident(), 4, "2026-03-13"));

    rest
      .perform(post("/api/parking/reservations/" + reservationId + "/cancellation"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status").value("CANCELLED"));
  }

  @Test
  void shouldNotCancelReservationLessThan24HoursBefore() throws Exception {
    var reservationId = idOf(reserve(uniqueResident(), 5, "2026-03-11"));

    rest.perform(post("/api/parking/reservations/" + reservationId + "/cancellation")).andExpect(status().isConflict());
  }

  @Test
  void shouldNotCancelUnknownReservation() throws Exception {
    rest
      .perform(post("/api/parking/reservations/" + UUID.randomUUID() + "/cancellation"))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.title").value("Unknown element"));
  }

  @Test
  void shouldListAvailableSpaces() throws Exception {
    reserve(uniqueResident(), 6, "2026-03-17").andExpect(status().isCreated());

    rest
      .perform(get("/api/parking/spaces/available").param("date", "2026-03-17"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", contains(1, 2, 3, 4, 5, 7, 8, 9, 10)));
  }

  @Test
  void shouldListCurrentReservationsOfResident() throws Exception {
    var resident = uniqueResident();
    reserve(resident, 7, "2026-03-15").andExpect(status().isCreated());
    reserve(resident, 8, "2026-03-14").andExpect(status().isCreated());

    rest
      .perform(get("/api/parking/reservations").param("resident", " " + resident.toUpperCase() + " "))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[*].date", contains("2026-03-14", "2026-03-15")));
  }

  private ResultActions reserve(String resident, int space, String date) throws Exception {
    var body = """
    {
      "residentName": "%s",
      "spaceNumber": %d,
      "date": "%s"
    }
    """.formatted(resident, space, date);

    return rest.perform(post("/api/parking/reservations").contentType(MediaType.APPLICATION_JSON).content(body));
  }

  private String idOf(ResultActions result) throws Exception {
    return JsonPath.read(result.andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), "$.id");
  }

  private static String uniqueResident() {
    return "Resident " + UUID.randomUUID().toString().substring(0, 8);
  }
}

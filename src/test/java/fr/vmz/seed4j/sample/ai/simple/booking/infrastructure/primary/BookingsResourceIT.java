package fr.vmz.seed4j.sample.ai.simple.booking.infrastructure.primary;

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
class BookingsResourceIT {

  // Each test books its own slots: in-memory bookings are shared by the whole Spring context
  private static final String HAIRCUT = "00000000-0000-4000-8000-000000000001";

  @Autowired
  private MockMvc rest;

  @Test
  void shouldListServices() throws Exception {
    rest
      .perform(get("/api/booking/services"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].id").value(HAIRCUT))
      .andExpect(jsonPath("$[0].name").value("Haircut"))
      .andExpect(jsonPath("$[0].durationInMinutes").value(30));
  }

  @Test
  void shouldBookSlot() throws Exception {
    postJson("/api/booking/bookings", booking(HAIRCUT, "2026-03-12T10:00:00", "0612345678"))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id").isNotEmpty())
      .andExpect(jsonPath("$.serviceId").value(HAIRCUT))
      .andExpect(jsonPath("$.start").value("2026-03-12T10:00:00"))
      .andExpect(jsonPath("$.end").value("2026-03-12T10:30:00"))
      .andExpect(jsonPath("$.customerName").value("Jane Doe"))
      .andExpect(jsonPath("$.phone").value("0612345678"))
      .andExpect(jsonPath("$.status").value("CONFIRMED"));
  }

  @Test
  void shouldViewBooking() throws Exception {
    var bookingId = book("2026-03-12T11:00:00");

    rest
      .perform(get("/api/booking/bookings/" + bookingId))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.start").value("2026-03-12T11:00:00"));
  }

  @Test
  void shouldNotViewUnknownBooking() throws Exception {
    rest
      .perform(get("/api/booking/bookings/" + UUID.randomUUID()))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.title").value("Unknown element"));
  }

  @Test
  void shouldNotBookUnknownService() throws Exception {
    postJson("/api/booking/bookings", booking(UUID.randomUUID().toString(), "2026-03-12T10:00:00", "0612345678")).andExpect(
      status().isNotFound()
    );
  }

  @Test
  void shouldNotBookAlreadyBookedSlot() throws Exception {
    book("2026-03-13T10:00:00");

    postJson("/api/booking/bookings", booking(HAIRCUT, "2026-03-13T10:00:00", "0712345678"))
      .andExpect(status().isConflict())
      .andExpect(jsonPath("$.title").value("Business rule violation"));
  }

  @Test
  void shouldNotBookOnSunday() throws Exception {
    postJson("/api/booking/bookings", booking(HAIRCUT, "2026-03-15T10:00:00", "0612345678"))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.title").value("Invalid value"));
  }

  @Test
  void shouldNotBookWithoutMobilePhone() throws Exception {
    postJson("/api/booking/bookings", booking(HAIRCUT, "2026-03-12T14:00:00", "0412345678"))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.detail").value(containsString("0412345678")));
  }

  @Test
  void shouldNotBookWithoutStart() throws Exception {
    postJson("/api/booking/bookings", "{\"serviceId\":\"" + HAIRCUT + "\",\"customerName\":\"Jane Doe\",\"phone\":\"0612345678\"}")
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.detail").value(containsString("start")));
  }

  @Test
  void shouldNotBookPastSlot() throws Exception {
    postJson("/api/booking/bookings", booking(HAIRCUT, "2026-03-10T09:30:00", "0612345678")).andExpect(status().isConflict());
  }

  @Test
  void shouldChangeSlot() throws Exception {
    var bookingId = book("2026-03-14T10:00:00");

    putJson("/api/booking/bookings/" + bookingId + "/slot", "{\"start\":\"2026-03-14T15:00:00\"}")
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.start").value("2026-03-14T15:00:00"));
  }

  @Test
  void shouldCancelBooking() throws Exception {
    var bookingId = book("2026-03-16T10:00:00");

    rest
      .perform(post("/api/booking/bookings/" + bookingId + "/cancellation"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status").value("CANCELLED"));
  }

  @Test
  void shouldNotCancelCancelledBooking() throws Exception {
    var bookingId = book("2026-03-16T11:00:00");
    rest.perform(post("/api/booking/bookings/" + bookingId + "/cancellation"));

    rest.perform(post("/api/booking/bookings/" + bookingId + "/cancellation")).andExpect(status().isConflict());
  }

  private String book(String start) throws Exception {
    var result = postJson("/api/booking/bookings", booking(HAIRCUT, start, "0612345678")).andExpect(status().isCreated()).andReturn();

    return JsonPath.read(result.getResponse().getContentAsString(), "$.id");
  }

  private ResultActions postJson(String url, String body) throws Exception {
    return rest.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(body));
  }

  private ResultActions putJson(String url, String body) throws Exception {
    return rest.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(body));
  }

  private static String booking(String serviceId, String start, String phone) {
    return """
    {
      "serviceId": "%s",
      "start": "%s",
      "customerName": "Jane Doe",
      "phone": "%s"
    }
    """.formatted(serviceId, start, phone);
  }
}

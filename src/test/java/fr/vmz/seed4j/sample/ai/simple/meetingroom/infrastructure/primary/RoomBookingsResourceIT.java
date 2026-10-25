package fr.vmz.seed4j.sample.ai.simple.meetingroom.infrastructure.primary;

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
class RoomBookingsResourceIT {

  // In-memory data is shared by the whole Spring context: each test creates its own room and employee
  @Autowired
  private MockMvc rest;

  @Test
  void shouldBookRoom() throws Exception {
    var roomId = createRoom();

    book(roomId, "2026-03-11T10:00:00", "Jane.Doe@corp.com")
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id").isNotEmpty())
      .andExpect(jsonPath("$.roomId").value(roomId))
      .andExpect(jsonPath("$.start").value("2026-03-11T10:00:00"))
      .andExpect(jsonPath("$.end").value("2026-03-11T11:00:00"))
      .andExpect(jsonPath("$.employeeEmail").value("jane.doe@corp.com"))
      .andExpect(jsonPath("$.status").value("ACTIVE"));
  }

  @Test
  void shouldNotBookRoomTwiceOnTheSameSlot() throws Exception {
    var roomId = createRoom();
    book(roomId, "2026-03-11T10:00:00", uniqueEmployee()).andExpect(status().isCreated());

    book(roomId, "2026-03-11T10:00:00", uniqueEmployee())
      .andExpect(status().isConflict())
      .andExpect(jsonPath("$.title").value("Business rule violation"));
  }

  @Test
  void shouldBookSlotFreedByCancellation() throws Exception {
    var roomId = createRoom();
    var employee = uniqueEmployee();
    var bookingId = idOf(book(roomId, "2026-03-11T10:00:00", employee));
    cancel(bookingId, employee).andExpect(status().isOk());

    book(roomId, "2026-03-11T10:00:00", uniqueEmployee()).andExpect(status().isCreated());
  }

  @Test
  void shouldNotBookHalfHourSlot() throws Exception {
    book(createRoom(), "2026-03-11T10:30:00", uniqueEmployee())
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.title").value("Invalid value"));
  }

  @Test
  void shouldNotBookWithInvalidEmail() throws Exception {
    book(createRoom(), "2026-03-11T10:00:00", "jane").andExpect(status().isBadRequest());
  }

  @Test
  void shouldNotBookUnknownRoom() throws Exception {
    book(UUID.randomUUID().toString(), "2026-03-11T10:00:00", uniqueEmployee())
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.title").value("Unknown element"));
  }

  @Test
  void shouldNotBookStartedSlot() throws Exception {
    book(createRoom(), "2026-03-10T10:00:00", uniqueEmployee()).andExpect(status().isConflict());
  }

  @Test
  void shouldCancelBooking() throws Exception {
    var employee = uniqueEmployee();
    var bookingId = idOf(book(createRoom(), "2026-03-11T10:00:00", employee));

    cancel(bookingId, employee).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("CANCELLED"));
  }

  @Test
  void shouldNotCancelBookingOfAnotherEmployee() throws Exception {
    var bookingId = idOf(book(createRoom(), "2026-03-11T10:00:00", uniqueEmployee()));

    cancel(bookingId, uniqueEmployee()).andExpect(status().isConflict());
  }

  @Test
  void shouldNotCancelUnknownBooking() throws Exception {
    cancel(UUID.randomUUID().toString(), uniqueEmployee()).andExpect(status().isNotFound());
  }

  @Test
  void shouldListCurrentBookingsOfEmployee() throws Exception {
    var roomId = createRoom();
    var employee = uniqueEmployee();
    book(roomId, "2026-03-12T09:00:00", employee).andExpect(status().isCreated());
    book(roomId, "2026-03-11T15:00:00", employee).andExpect(status().isCreated());

    rest
      .perform(get("/api/meeting-rooms/bookings").param("employee", employee.toUpperCase()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[*].start", contains("2026-03-11T15:00:00", "2026-03-12T09:00:00")));
  }

  private String createRoom() throws Exception {
    var body = "{\"name\":\"Room " + UUID.randomUUID().toString().substring(0, 8) + "\"}";

    return idOf(rest.perform(post("/api/meeting-rooms/rooms").contentType(MediaType.APPLICATION_JSON).content(body)));
  }

  private ResultActions book(String roomId, String start, String employee) throws Exception {
    var body = """
    {
      "roomId": "%s",
      "start": "%s",
      "employeeEmail": "%s"
    }
    """.formatted(roomId, start, employee);

    return rest.perform(post("/api/meeting-rooms/bookings").contentType(MediaType.APPLICATION_JSON).content(body));
  }

  private ResultActions cancel(String bookingId, String employee) throws Exception {
    return rest.perform(
      post("/api/meeting-rooms/bookings/" + bookingId + "/cancellation")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"employeeEmail\":\"" + employee + "\"}")
    );
  }

  private String idOf(ResultActions result) throws Exception {
    return JsonPath.read(result.andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), "$.id");
  }

  private static String uniqueEmployee() {
    return "employee-" + UUID.randomUUID().toString().substring(0, 8) + "@corp.com";
  }
}

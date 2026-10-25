package fr.vmz.seed4j.sample.ai.simple.meetingroom.infrastructure.primary;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.vmz.seed4j.sample.ai.IntegrationTest;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@IntegrationTest
@AutoConfigureMockMvc
class RoomsResourceIT {

  // In-memory rooms are shared by the whole Spring context: each test uses its own room names
  @Autowired
  private MockMvc rest;

  @Test
  void shouldCreateRoom() throws Exception {
    var name = uniqueName();

    createRoom(name)
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id").isNotEmpty())
      .andExpect(jsonPath("$.name").value(name));
  }

  @Test
  void shouldNotCreateTwoRoomsWithSameName() throws Exception {
    var name = uniqueName();
    createRoom(name).andExpect(status().isCreated());

    createRoom(name.toUpperCase())
      .andExpect(status().isConflict())
      .andExpect(jsonPath("$.title").value("Business rule violation"));
  }

  @Test
  void shouldNotCreateRoomWithoutName() throws Exception {
    createRoom(" ").andExpect(status().isBadRequest()).andExpect(jsonPath("$.title").value("Invalid value"));
  }

  @Test
  void shouldListRooms() throws Exception {
    var name = uniqueName();
    createRoom(name);

    rest.perform(get("/api/meeting-rooms/rooms")).andExpect(status().isOk()).andExpect(jsonPath("$[*].name", hasItem(name)));
  }

  private ResultActions createRoom(String name) throws Exception {
    return rest.perform(post("/api/meeting-rooms/rooms").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"" + name + "\"}"));
  }

  private static String uniqueName() {
    return "Room " + UUID.randomUUID().toString().substring(0, 8);
  }
}

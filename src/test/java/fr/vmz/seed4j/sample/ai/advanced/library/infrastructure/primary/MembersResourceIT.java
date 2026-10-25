package fr.vmz.seed4j.sample.ai.advanced.library.infrastructure.primary;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
class MembersResourceIT {

  @Autowired
  private MockMvc rest;

  @Autowired
  private MovableClock clock;

  private LibraryApi api;

  @BeforeEach
  void loadApi() {
    api = new LibraryApi(rest);
    clock.today(LocalDate.of(2026, 3, 10));
  }

  @Test
  void shouldRegisterMemberToday() throws Exception {
    var email = LibraryApi.uniqueEmail();

    api
      .registerMember(email.toUpperCase())
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id").isNotEmpty())
      .andExpect(jsonPath("$.fullName").value("Jane Doe"))
      .andExpect(jsonPath("$.email").value(email))
      .andExpect(jsonPath("$.registeredOn").value("2026-03-10"))
      .andExpect(jsonPath("$.status").value("ACTIVE"));
  }

  @Test
  void shouldNotRegisterTwoMembersWithTheSameEmail() throws Exception {
    var email = LibraryApi.uniqueEmail();
    api.registerMember(email).andExpect(status().isCreated());

    api.registerMember(email).andExpect(status().isConflict()).andExpect(jsonPath("$.title").value("Business rule violation"));
  }

  @Test
  void shouldNotRegisterMemberWithInvalidEmail() throws Exception {
    api.registerMember("jane@mail").andExpect(status().isBadRequest()).andExpect(jsonPath("$.title").value("Invalid value"));
  }

  @Test
  void shouldNotRegisterMemberWithoutName() throws Exception {
    api
      .postJson("/api/library/members", "{\"email\":\"" + LibraryApi.uniqueEmail() + "\"}")
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.detail").value(containsString("name")));
  }

  @Test
  void shouldViewMember() throws Exception {
    var memberId = api.memberId();

    rest.perform(get("/api/library/members/" + memberId)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(memberId));
  }

  @Test
  void shouldNotViewUnknownMember() throws Exception {
    rest
      .perform(get("/api/library/members/" + UUID.randomUUID()))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.title").value("Unknown element"));
  }
}

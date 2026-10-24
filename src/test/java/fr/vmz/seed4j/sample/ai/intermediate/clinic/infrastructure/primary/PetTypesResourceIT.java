package fr.vmz.seed4j.sample.ai.intermediate.clinic.infrastructure.primary;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.vmz.seed4j.sample.ai.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTest
@AutoConfigureMockMvc
class PetTypesResourceIT {

  @Autowired
  private MockMvc rest;

  @Test
  void shouldListPetTypes() throws Exception {
    rest
      .perform(get("/api/pet-types"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", contains("CAT", "DOG", "LIZARD", "SNAKE", "BIRD", "HAMSTER")));
  }
}

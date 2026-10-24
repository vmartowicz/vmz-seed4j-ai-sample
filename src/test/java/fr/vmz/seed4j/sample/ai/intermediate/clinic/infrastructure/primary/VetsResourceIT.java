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
class VetsResourceIT {

  @Autowired
  private MockMvc rest;

  @Test
  void shouldListVetsWithSpecialties() throws Exception {
    rest
      .perform(get("/api/vets"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content", hasSize(5)))
      .andExpect(jsonPath("$.content[0].id").isNotEmpty())
      .andExpect(jsonPath("$.content[0].firstName").value("James"))
      .andExpect(jsonPath("$.content[0].lastName").value("Carter"))
      .andExpect(jsonPath("$.content[0].specialties").isEmpty())
      .andExpect(jsonPath("$.content[1].specialties", contains("SURGERY", "DENTISTRY")))
      .andExpect(jsonPath("$.page").value(1))
      .andExpect(jsonPath("$.totalElements").value(6))
      .andExpect(jsonPath("$.totalPages").value(2));
  }

  @Test
  void shouldListSecondPageOfVets() throws Exception {
    rest
      .perform(get("/api/vets").param("page", "2"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content[*].lastName", contains("Stevens")));
  }
}

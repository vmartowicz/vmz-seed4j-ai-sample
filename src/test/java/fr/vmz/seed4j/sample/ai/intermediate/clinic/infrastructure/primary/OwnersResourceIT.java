package fr.vmz.seed4j.sample.ai.intermediate.clinic.infrastructure.primary;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jayway.jsonpath.JsonPath;
import fr.vmz.seed4j.sample.ai.IntegrationTest;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
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
class OwnersResourceIT {

  @Autowired
  private MockMvc rest;

  @Nested
  class Owners {

    @Test
    void shouldRegisterOwner() throws Exception {
      postJson("/api/owners", owner("George", "Franklin", "6085551023"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.firstName").value("George"))
        .andExpect(jsonPath("$.lastName").value("Franklin"))
        .andExpect(jsonPath("$.address").value("110 W. Liberty St."))
        .andExpect(jsonPath("$.city").value("Madison"))
        .andExpect(jsonPath("$.telephone").value("6085551023"))
        .andExpect(jsonPath("$.pets").isEmpty());
    }

    @Test
    void shouldNotRegisterOwnerWithInvalidTelephone() throws Exception {
      postJson("/api/owners", owner("George", "Franklin", "608555102"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.title").value("Invalid value"))
        .andExpect(jsonPath("$.detail").value(containsString("608555102")));
    }

    @Test
    void shouldNotRegisterOwnerWithoutFirstName() throws Exception {
      postJson("/api/owners", owner("", "Franklin", "6085551023"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.title").value("Invalid value"))
        .andExpect(jsonPath("$.detail").value(containsString("firstName")));
    }

    @Test
    void shouldUpdateOwner() throws Exception {
      var ownerId = registerOwner("Franklin");

      putJson("/api/owners/" + ownerId, owner("Betty", "Davis", "6085551749"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(ownerId))
        .andExpect(jsonPath("$.firstName").value("Betty"))
        .andExpect(jsonPath("$.telephone").value("6085551749"));
    }

    @Test
    void shouldNotGetUnknownOwner() throws Exception {
      rest
        .perform(get("/api/owners/" + UUID.randomUUID()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.title").value("Unknown element"));
    }

    @Test
    void shouldGetOwnerWithPetsAndVisits() throws Exception {
      var ownerId = registerOwner("Franklin");
      var petId = addPet(ownerId, "Leo");
      postJson("/api/owners/" + ownerId + "/pets/" + petId + "/visits", visit("2026-03-11"));

      rest
        .perform(get("/api/owners/" + ownerId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lastName").value("Franklin"))
        .andExpect(jsonPath("$.pets[0].name").value("Leo"))
        .andExpect(jsonPath("$.pets[0].birthDate").value("2020-09-07"))
        .andExpect(jsonPath("$.pets[0].type").value("CAT"))
        .andExpect(jsonPath("$.pets[0].visits[0].date").value("2026-03-11"))
        .andExpect(jsonPath("$.pets[0].visits[0].description").value("Rabies shot"));
    }
  }

  @Nested
  class Search {

    @Test
    void shouldSearchOwnersByLastNamePrefix() throws Exception {
      var lastNamePrefix = uniqueLastName();
      var ownerId = registerOwner(lastNamePrefix + "son");
      addPet(ownerId, "Leo");
      registerOwner(lastNamePrefix + "ston");

      rest
        .perform(get("/api/owners").param("lastName", lastNamePrefix.toLowerCase()).param("page", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(2)))
        .andExpect(jsonPath("$.content[0].id").value(ownerId))
        .andExpect(jsonPath("$.content[0].pets[0].name").value("Leo"))
        .andExpect(jsonPath("$.page").value(1))
        .andExpect(jsonPath("$.pageSize").value(5))
        .andExpect(jsonPath("$.totalElements").value(2))
        .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void shouldGetFirstPageOfAllOwnersWithoutLastName() throws Exception {
      registerOwner(uniqueLastName());

      rest
        .perform(get("/api/owners"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.page").value(1))
        .andExpect(jsonPath("$.totalElements").value(greaterThanOrEqualTo(1)));
    }

    @Test
    void shouldGetEmptyPageForUnknownLastName() throws Exception {
      rest
        .perform(get("/api/owners").param("lastName", uniqueLastName()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isEmpty())
        .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    void shouldNotSearchPageZero() throws Exception {
      rest
        .perform(get("/api/owners").param("page", "0"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.title").value("Invalid value"));
    }

    private String uniqueLastName() {
      return "Zz" + UUID.randomUUID().toString().substring(0, 8);
    }
  }

  @Nested
  class Pets {

    @Test
    void shouldAddPet() throws Exception {
      var ownerId = registerOwner("Franklin");

      postJson("/api/owners/" + ownerId + "/pets", pet("Leo", "2026-03-10", "\"DOG\""))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.name").value("Leo"))
        .andExpect(jsonPath("$.birthDate").value("2026-03-10"))
        .andExpect(jsonPath("$.type").value("DOG"))
        .andExpect(jsonPath("$.visits").isEmpty());
    }

    @Test
    void shouldNotAddPetWithDuplicatedName() throws Exception {
      var ownerId = registerOwner("Franklin");
      addPet(ownerId, "Leo");

      postJson("/api/owners/" + ownerId + "/pets", pet("leo", "2020-09-07", "\"CAT\""))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.title").value("Business rule violation"));
    }

    @Test
    void shouldNotAddPetWithoutType() throws Exception {
      var ownerId = registerOwner("Franklin");

      postJson("/api/owners/" + ownerId + "/pets", pet("Leo", "2020-09-07", "null"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.detail").value(containsString("type")));
    }

    @Test
    void shouldNotAddPetBornInTheFuture() throws Exception {
      var ownerId = registerOwner("Franklin");

      postJson("/api/owners/" + ownerId + "/pets", pet("Leo", "2026-03-11", "\"CAT\""))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.title").value("Invalid value"));
    }

    @Test
    void shouldNotAddPetToUnknownOwner() throws Exception {
      postJson("/api/owners/" + UUID.randomUUID() + "/pets", pet("Leo", "2020-09-07", "\"CAT\"")).andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdatePet() throws Exception {
      var ownerId = registerOwner("Franklin");
      var petId = addPet(ownerId, "Leo");

      putJson("/api/owners/" + ownerId + "/pets/" + petId, pet("Rosy", "2021-04-17", "\"HAMSTER\""))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(petId))
        .andExpect(jsonPath("$.name").value("Rosy"))
        .andExpect(jsonPath("$.type").value("HAMSTER"));
    }
  }

  @Nested
  class Visits {

    @Test
    void shouldScheduleVisitTomorrow() throws Exception {
      var ownerId = registerOwner("Franklin");
      var petId = addPet(ownerId, "Leo");

      postJson("/api/owners/" + ownerId + "/pets/" + petId + "/visits", visit("2026-03-11"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.date").value("2026-03-11"))
        .andExpect(jsonPath("$.description").value("Rabies shot"));
    }

    @Test
    void shouldNotScheduleVisitToday() throws Exception {
      var ownerId = registerOwner("Franklin");
      var petId = addPet(ownerId, "Leo");

      postJson("/api/owners/" + ownerId + "/pets/" + petId + "/visits", visit("2026-03-10"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.title").value("Invalid value"));
    }

    @Test
    void shouldNotScheduleVisitForPetOfAnotherOwner() throws Exception {
      var petId = addPet(registerOwner("Franklin"), "Leo");
      var anotherOwnerId = registerOwner("Davis");

      postJson("/api/owners/" + anotherOwnerId + "/pets/" + petId + "/visits", visit("2026-03-11"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.title").value("Unknown element"));
    }
  }

  private String registerOwner(String lastName) throws Exception {
    return idOf(postJson("/api/owners", owner("George", lastName, "6085551023")));
  }

  private String addPet(String ownerId, String name) throws Exception {
    return idOf(postJson("/api/owners/" + ownerId + "/pets", pet(name, "2020-09-07", "\"CAT\"")));
  }

  private String idOf(ResultActions result) throws Exception {
    return JsonPath.read(result.andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), "$.id");
  }

  private ResultActions postJson(String url, String body) throws Exception {
    return rest.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(body));
  }

  private ResultActions putJson(String url, String body) throws Exception {
    return rest.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(body));
  }

  private static String owner(String firstName, String lastName, String telephone) {
    return """
    {
      "firstName": "%s",
      "lastName": "%s",
      "address": "110 W. Liberty St.",
      "city": "Madison",
      "telephone": "%s"
    }
    """.formatted(firstName, lastName, telephone);
  }

  private static String pet(String name, String birthDate, String type) {
    return """
    {
      "name": "%s",
      "birthDate": "%s",
      "type": %s
    }
    """.formatted(name, birthDate, type);
  }

  private static String visit(String date) {
    return """
    {
      "date": "%s",
      "description": "Rabies shot"
    }
    """.formatted(date);
  }
}

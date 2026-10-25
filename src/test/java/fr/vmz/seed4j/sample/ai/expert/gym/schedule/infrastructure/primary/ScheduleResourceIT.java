package fr.vmz.seed4j.sample.ai.expert.gym.schedule.infrastructure.primary;

import static fr.vmz.seed4j.sample.ai.expert.gym.GymApi.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.vmz.seed4j.sample.ai.IntegrationTest;
import fr.vmz.seed4j.sample.ai.expert.gym.GymApi;
import fr.vmz.seed4j.sample.ai.expert.gym.GymClockConfiguration;
import java.time.DayOfWeek;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@IntegrationTest
@AutoConfigureMockMvc
@Import(GymClockConfiguration.class)
class ScheduleResourceIT {

  @Autowired
  private MockMvc rest;

  private GymApi api;

  @BeforeEach
  void loadApi() {
    api = new GymApi(rest);
  }

  @Test
  void shouldRegisterCoach() throws Exception {
    api
      .postJson("/api/gym/schedule/coaches", "{\"name\":\"Alice Martin\",\"certifications\":[\"YOGA\",\"PILATES\"]}")
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id").isNotEmpty())
      .andExpect(jsonPath("$.name").value("Alice Martin"))
      .andExpect(jsonPath("$.certifications").value(containsInAnyOrder("YOGA", "PILATES")));
  }

  @Test
  void shouldScheduleWeeklyClassAndPublishItsSessions() throws Exception {
    var coach = api.coach("YOGA");
    var place = freeStudioDay("ECULLY");
    var gymClass = read(
      api
        .scheduleClass(coach, place, "18:00", 60, 15)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.discipline").value("YOGA"))
        .andExpect(jsonPath("$.coachId").value(coach))
        .andExpect(jsonPath("$.gym").value("ECULLY"))
        .andExpect(jsonPath("$.studio").value(place.studio()))
        .andExpect(jsonPath("$.day").value(place.day().name()))
        .andExpect(jsonPath("$.start").value(startsWith("18:00")))
        .andExpect(jsonPath("$.durationMinutes").value(60))
        .andExpect(jsonPath("$.capacity").value(15)),
      "$.id"
    );
    var monday = freeMonday();

    api.publish(monday).andExpect(status().isOk()).andExpect(jsonPath("$[?(@.classId == '" + gymClass + "')].start").value(contains(startsWith(place.at(monday, "18:00").toString()))));

    api
      .get("/api/gym/schedule/weeks/" + monday + "/sessions")
      .andExpect(jsonPath("$[?(@.classId == '" + gymClass + "')].end").value(contains(startsWith(place.at(monday, "19:00").toString()))))
      .andExpect(jsonPath("$[?(@.classId == '" + gymClass + "')].cancelled").value(contains(false)));
  }

  @Test
  void shouldNotPublishWeekTwice() throws Exception {
    api.yogaClass(freeStudioDay("ECULLY"), "08:00", 10);
    var monday = freeMonday();
    api.publish(monday).andExpect(status().isOk());

    api.publish(monday).andExpect(status().isConflict());
  }

  @Test
  void shouldPublishOnlyFromMonday() throws Exception {
    api.publish(freeMonday().plusDays(1)).andExpect(status().isBadRequest()).andExpect(jsonPath("$.title").value("Invalid value"));
  }

  @Test
  void shouldNotScheduleClassWithUncertifiedCoach() throws Exception {
    api
      .scheduleClass(api.coach("BOXING"), freeStudioDay("VILLEURBANNE"), "10:00", 60, 10)
      .andExpect(status().isConflict())
      .andExpect(jsonPath("$.detail").value(containsString("YOGA")));
  }

  @Test
  void shouldNotExceedStudioCapacity() throws Exception {
    api.scheduleClass(api.coach("YOGA"), freeStudioDay("VILLEURBANNE"), "10:00", 60, 31).andExpect(status().isConflict());
  }

  @Test
  void shouldNotScheduleClassWithoutPlace() throws Exception {
    api.scheduleClass(api.coach("YOGA"), freeStudioDay("VILLEURBANNE"), "10:00", 60, 0).andExpect(status().isBadRequest());
  }

  @Test
  void shouldNotScheduleClassOfOtherDuration() throws Exception {
    api
      .scheduleClass(api.coach("YOGA"), freeStudioDay("CONFLUENCE"), "10:00", 30, 10)
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.detail").value(containsString("45 or 60")));
  }

  @Test
  void shouldNotScheduleClassInUnknownStudio() throws Exception {
    var place = new StudioDay("ECULLY", "C", DayOfWeek.MONDAY);

    api.scheduleClass(api.coach("YOGA"), place, "10:00", 60, 10).andExpect(status().isNotFound());
  }

  @Test
  void shouldNotUseBusyStudio() throws Exception {
    var place = freeStudioDay("CONFLUENCE");
    api.yogaClass(place, "18:00", 10);

    api.scheduleClass(api.coach("YOGA"), place, "18:30", 45, 10).andExpect(status().isConflict());
  }

  @Test
  void shouldKeep15MinutesBetweenClassesOfCoach() throws Exception {
    var coach = api.coach("YOGA");
    var place = freeStudioDay("CONFLUENCE");
    api.scheduleClass(coach, place, "10:00", 60, 10).andExpect(status().isCreated());

    api.scheduleClass(coach, place, "11:10", 60, 10).andExpect(status().isConflict()).andExpect(jsonPath("$.detail").value(containsString("not available")));
    api.scheduleClass(coach, place, "11:15", 60, 10).andExpect(status().isCreated());
  }

  @Test
  void shouldReplaceSickCoachWithCertifiedCoach() throws Exception {
    var gymClass = api.yogaClass(freeStudioDay("ECULLY"), "12:00", 10);
    var monday = freeMonday();
    api.publish(monday);
    var session = api.sessionOf(monday, gymClass);
    var replacement = api.coach("YOGA");

    replace(session, api.coach("BOXING")).andExpect(status().isConflict());
    replace(session, replacement).andExpect(status().isOk()).andExpect(jsonPath("$.coachId").value(replacement));
  }

  @Test
  void shouldNotFindUnknownSessionOrCoach() throws Exception {
    replace(UUID.randomUUID().toString(), api.coach("YOGA")).andExpect(status().isNotFound()).andExpect(jsonPath("$.title").value("Unknown element"));
    api.scheduleClass(UUID.randomUUID().toString(), freeStudioDay("VILLEURBANNE"), "10:00", 60, 10).andExpect(status().isNotFound());
  }

  private ResultActions replace(String session, String coach) throws Exception {
    return api.postJson("/api/gym/schedule/sessions/" + session + "/coach-replacement", "{\"coachId\":\"" + coach + "\"}");
  }
}

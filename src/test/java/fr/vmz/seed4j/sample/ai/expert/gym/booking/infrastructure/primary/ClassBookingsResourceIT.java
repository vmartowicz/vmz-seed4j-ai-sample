package fr.vmz.seed4j.sample.ai.expert.gym.booking.infrastructure.primary;

import static fr.vmz.seed4j.sample.ai.expert.gym.GymApi.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jayway.jsonpath.JsonPath;
import fr.vmz.seed4j.sample.ai.IntegrationTest;
import fr.vmz.seed4j.sample.ai.expert.gym.GymApi;
import fr.vmz.seed4j.sample.ai.expert.gym.GymClock;
import fr.vmz.seed4j.sample.ai.expert.gym.GymClockConfiguration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
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
class ClassBookingsResourceIT {

  @Autowired
  private MockMvc rest;

  @Autowired
  private GymClock clock;

  private GymApi api;
  private LocalDate monday;

  @BeforeEach
  void loadApi() {
    api = new GymApi(rest);
    monday = freeMonday();
    clock.at(monday.minusWeeks(5).atTime(9, 0));
  }

  @Test
  void shouldBookClass() throws Exception {
    var place = freeStudioDay("PART_DIEU");
    var session = session(place, "18:00", 10);
    var member = api.activeMember("ESSENTIAL", "PART_DIEU");
    clock.at(place.at(monday, "18:00").minusDays(1));

    var booking = read(
      api
        .book(member.id(), session)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.memberId").value(member.id()))
        .andExpect(jsonPath("$.sessionId").value(session))
        .andExpect(jsonPath("$.gym").value("PART_DIEU"))
        .andExpect(jsonPath("$.start").value(startsWith(place.at(monday, "18:00").toString())))
        .andExpect(jsonPath("$.status").value("BOOKED")),
      "$.id"
    );

    api.get("/api/gym/bookings/" + booking).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("BOOKED"));
    api.get("/api/gym/bookings?sessionId=" + session).andExpect(jsonPath("$[*].id").value(contains(booking)));
  }

  @Test
  void shouldGiveFreedSeatToFirstWaitingMember() throws Exception {
    var place = freeStudioDay("CONFLUENCE");
    var session = session(place, "19:00", 1);
    var first = api.activeMember("PREMIUM", "ECULLY");
    var second = api.activeMember("ESSENTIAL", "CONFLUENCE");
    clock.at(place.at(monday, "19:00").minusDays(2));
    var seat = read(api.book(first.id(), session), "$.id");
    var waiting = read(api.book(second.id(), session).andExpect(jsonPath("$.status").value("WAITING")), "$.id");

    step(seat, "cancellation").andExpect(status().isOk()).andExpect(jsonPath("$.status").value("CANCELLED"));

    api.get("/api/gym/bookings/" + waiting).andExpect(jsonPath("$.status").value("BOOKED"));
  }

  @Test
  void shouldOpenBookings7DaysBeforeAndCloseThem1HourBefore() throws Exception {
    var place = freeStudioDay("PART_DIEU");
    var session = session(place, "18:00", 10);
    var member = api.activeMember("ESSENTIAL", "PART_DIEU");
    var start = place.at(monday, "18:00");

    clock.at(start.minusDays(7).minusMinutes(1));
    api.book(member.id(), session).andExpect(status().isConflict()).andExpect(jsonPath("$.detail").value(containsString("7 days")));
    clock.at(start.minusMinutes(59));
    api.book(member.id(), session).andExpect(status().isConflict()).andExpect(jsonPath("$.detail").value(containsString("1 hour")));
  }

  @Test
  void shouldNotBookOutsideHomeGymWithEssentialPlan() throws Exception {
    var place = freeStudioDay("PART_DIEU");
    var session = session(place, "18:00", 10);
    var member = api.activeMember("ESSENTIAL", "ECULLY");
    clock.at(place.at(monday, "18:00").minusDays(1));

    api.book(member.id(), session).andExpect(status().isConflict()).andExpect(jsonPath("$.detail").value(containsString("home gym")));
  }

  @Test
  void shouldLimitEssentialMembersTo4ClassesAMonth() throws Exception {
    var place = freeStudioDay("VILLEURBANNE");
    var classes = new String[] { "08:00", "09:00", "10:00", "11:00", "12:00" };
    for (var time : classes) {
      api.yogaClass(place, time, 10);
    }
    var sessions = publishedSessions(place, classes);
    var member = api.activeMember("ESSENTIAL", "VILLEURBANNE");
    clock.at(place.at(monday, "08:00").minusDays(1));
    for (int booking = 0; booking < 4; booking++) {
      api.book(member.id(), sessions[booking]).andExpect(status().isCreated());
    }

    api.book(member.id(), sessions[4]).andExpect(status().isConflict()).andExpect(jsonPath("$.detail").value(containsString("4 classes")));
  }

  @Test
  void shouldBanMemberFor7DaysAfter3NoShows() throws Exception {
    var place = freeStudioDay("ECULLY");
    var classes = new String[] { "08:00", "09:00", "10:00", "13:00" };
    for (var time : classes) {
      api.yogaClass(place, time, 10);
    }
    var sessions = publishedSessions(place, classes);
    var member = api.activeMember("PREMIUM", "ECULLY");
    clock.at(place.at(monday, "08:00").minusDays(1));
    var bookings = new ArrayList<String>();
    for (int session = 0; session < 3; session++) {
      bookings.add(read(api.book(member.id(), sessions[session]).andExpect(status().isCreated()), "$.id"));
    }
    clock.at(place.at(monday, "10:30"));
    for (var booking : bookings) {
      step(booking, "no-show").andExpect(status().isOk()).andExpect(jsonPath("$.status").value("NO_SHOW"));
    }

    api.book(member.id(), sessions[3]).andExpect(status().isConflict()).andExpect(jsonPath("$.detail").value(containsString("banned")));
  }

  @Test
  void shouldCountLateCancellationAsNoShow() throws Exception {
    var place = freeStudioDay("PART_DIEU");
    var session = session(place, "18:00", 10);
    var member = api.activeMember("PREMIUM", "PART_DIEU");
    clock.at(place.at(monday, "18:00").minusDays(1));
    var booking = read(api.book(member.id(), session), "$.id");
    clock.at(place.at(monday, "16:30"));

    step(booking, "cancellation").andExpect(jsonPath("$.status").value("LATE_CANCELLED"));
  }

  @Test
  void shouldNotMarkNoShowBeforeClassStarts() throws Exception {
    var place = freeStudioDay("PART_DIEU");
    var session = session(place, "18:00", 10);
    var member = api.activeMember("PREMIUM", "PART_DIEU");
    clock.at(place.at(monday, "18:00").minusDays(1));

    step(read(api.book(member.id(), session), "$.id"), "no-show").andExpect(status().isConflict());
  }

  @Test
  void shouldCancelBookingsWhenGymCancelsSession() throws Exception {
    var place = freeStudioDay("CONFLUENCE");
    var session = session(place, "18:00", 10);
    var member = api.activeMember("PREMIUM", "PART_DIEU");
    clock.at(place.at(monday, "18:00").minusDays(1));
    var booking = read(api.book(member.id(), session), "$.id");

    api.postJson("/api/gym/schedule/sessions/" + session + "/cancellation", "{}").andExpect(status().isOk()).andExpect(jsonPath("$.cancelled").value(true));

    api.get("/api/gym/bookings/" + booking).andExpect(jsonPath("$.status").value("CANCELLED_BY_GYM"));
    api.book(api.activeMember("PREMIUM", "ECULLY").id(), session).andExpect(status().isConflict());
  }

  @Test
  void shouldNotBookForMemberAwaitingFirstPayment() throws Exception {
    var place = freeStudioDay("PART_DIEU");
    var session = session(place, "18:00", 10);
    var member = api.member("PREMIUM", "PART_DIEU");
    clock.at(place.at(monday, "18:00").minusDays(1));

    api.book(member.id(), session).andExpect(status().isConflict()).andExpect(jsonPath("$.detail").value(containsString("not active")));
  }

  @Test
  void shouldNotBookUnknownSessionOrForUnknownMember() throws Exception {
    var member = api.activeMember("PREMIUM", "PART_DIEU");
    api.book(member.id(), UUID.randomUUID().toString()).andExpect(status().isNotFound());

    var place = freeStudioDay("VILLEURBANNE");
    var session = session(place, "18:00", 10);
    clock.at(place.at(monday, "18:00").minusDays(1));
    api.book(UUID.randomUUID().toString(), session).andExpect(status().isNotFound()).andExpect(jsonPath("$.title").value("Unknown element"));
  }

  @Test
  void shouldNotBookWithoutMember() throws Exception {
    api.postJson("/api/gym/bookings", "{\"sessionId\":\"" + UUID.randomUUID() + "\"}").andExpect(status().isBadRequest());
  }

  private String session(StudioDay place, String time, int capacity) throws Exception {
    api.yogaClass(place, time, capacity);

    return publishedSessions(place, time)[0];
  }

  private String[] publishedSessions(StudioDay place, String... times) throws Exception {
    var published = api.publish(monday).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    return Stream.of(times).map(time -> sessionAt(published, place, time)).toArray(String[]::new);
  }

  private String sessionAt(String published, StudioDay place, String time) {
    List<String> ids = JsonPath.read(
      published,
      "$[?(@.gym == '%s' && @.studio == '%s' && @.start =~ /%s.*/)].id".formatted(place.gym(), place.studio(), place.at(monday, time))
    );

    return ids.getFirst();
  }

  private ResultActions step(String booking, String step) throws Exception {
    return api.postJson("/api/gym/bookings/" + booking + "/" + step, "{}");
  }
}

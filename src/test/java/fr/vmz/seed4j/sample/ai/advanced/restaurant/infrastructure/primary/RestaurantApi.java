package fr.vmz.seed4j.sample.ai.advanced.restaurant.infrastructure.primary;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jayway.jsonpath.JsonPath;
import java.time.LocalDate;
import java.time.Month;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

final class RestaurantApi {

  private static final LocalDate FIRST_TUESDAY = LocalDate.of(2030, 1, 1);
  private static final Set<Month> SUMMER = Set.of(Month.JUNE, Month.JULY, Month.AUGUST);
  private static final AtomicInteger USED_WEEKS = new AtomicInteger();

  private final MockMvc rest;

  RestaurantApi(MockMvc rest) {
    this.rest = rest;
  }

  ResultActions book(String email, LocalDate date, String time, int people) throws Exception {
    return postJson(
      "/api/restaurant/bookings",
      """
      {"fullName":"Jane Doe","phone":"0612345678","email":"%s","date":"%s","time":"%s","people":%d,"comments":"Birthday"}
      """.formatted(email, date, time, people)
    );
  }

  String bookingId(String email, LocalDate date, String time, int people) throws Exception {
    return JsonPath.read(book(email, date, time, people).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), "$.id");
  }

  ResultActions step(String bookingId, String step) throws Exception {
    return postJson("/api/restaurant/bookings/" + bookingId + "/" + step, "{}");
  }

  ResultActions payDeposit(String bookingId, String amount) throws Exception {
    return postJson("/api/restaurant/bookings/" + bookingId + "/deposit-payment", "{\"amount\":" + amount + "}");
  }

  ResultActions get(String url) throws Exception {
    return rest.perform(MockMvcRequestBuilders.get(url));
  }

  ResultActions postJson(String url, String body) throws Exception {
    return rest.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(body));
  }

  static LocalDate freeTuesday() {
    return freeTuesday(false);
  }

  static LocalDate freeSummerTuesday() {
    return freeTuesday(true);
  }

  static String uniqueEmail() {
    return "diner-" + UUID.randomUUID().toString().substring(0, 8) + "@mail.com";
  }

  private static LocalDate freeTuesday(boolean summer) {
    LocalDate tuesday;
    do {
      tuesday = FIRST_TUESDAY.plusWeeks(USED_WEEKS.getAndIncrement());
    } while (SUMMER.contains(tuesday.getMonth()) != summer);

    return tuesday;
  }
}

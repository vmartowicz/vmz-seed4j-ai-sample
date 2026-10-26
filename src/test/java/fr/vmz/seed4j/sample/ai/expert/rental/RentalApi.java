package fr.vmz.seed4j.sample.ai.expert.rental;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jayway.jsonpath.JsonPath;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public final class RentalApi {

  public static final LocalDateTime NOW = LocalDateTime.of(2026, 6, 1, 10, 0);
  public static final LocalDateTime PICK_UP = LocalDateTime.of(2026, 6, 10, 9, 0);

  private static final AtomicInteger USED_PLATES = new AtomicInteger();

  private final MockMvc rest;

  public RentalApi(MockMvc rest) {
    this.rest = rest;
  }

  public ResultActions registerVehicle(String plate, String category, String fuel, String point) throws Exception {
    return postJson(
      "/api/rental/fleet/vehicles",
      """
      {"plate":"%s","make":"Renault","model":"Clio","category":"%s","fuel":"%s","mileage":20000,"firstRegistration":"2025-01-10","lastService":"2026-05-01","point":"%s"}
      """.formatted(plate, category, fuel, point)
    );
  }

  public String vehicle(String category, String point) throws Exception {
    var plate = freePlate();
    registerVehicle(plate, category, "PETROL", point).andExpect(status().isCreated());

    return plate;
  }

  public ResultActions book(String email, String birthDate, String category, LocalDateTime pickUpAt, int days, String returnPoint, String options)
    throws Exception {
    return postJson(
      "/api/rental/reservations",
      """
      {"driver":{"name":"Jane Doe","email":"%s","birthDate":"%s","licenceDate":"2015-01-01"},"category":"%s","pickUpPoint":"PART_DIEU","pickUpAt":"%s","returnPoint":"%s","returnAt":"%s"%s}
      """.formatted(email, birthDate, category, pickUpAt, returnPoint, pickUpAt.plusDays(days), options)
    );
  }

  public ResultActions book(String email, String category, int days) throws Exception {
    return book(email, "1990-05-04", category, PICK_UP, days, "PART_DIEU", "");
  }

  public String reservation(String email, String category, int days) throws Exception {
    return read(book(email, category, days).andExpect(status().isCreated()), "$.id");
  }

  public ResultActions pickUp(String reservation) throws Exception {
    return postJson("/api/rental/counter/pick-ups", "{\"reservationId\":\"" + reservation + "\"}");
  }

  public String contract(String reservation) throws Exception {
    return read(pickUp(reservation).andExpect(status().isCreated()), "$.id");
  }

  public ResultActions giveBack(String contract, String point, int kilometres, String missingFuel, String damage) throws Exception {
    var mileageOut = Integer.parseInt(read(get("/api/rental/counter/contracts/" + contract), "$.mileageOut"));
    var damageJson = damage == null ? "" : ",\"damage\":\"" + damage + "\"";

    return postJson(
      "/api/rental/counter/contracts/" + contract + "/return",
      "{\"point\":\"%s\",\"mileage\":%d,\"missingFuel\":%s%s}".formatted(point, mileageOut + kilometres, missingFuel, damageJson)
    );
  }

  public ResultActions get(String url) throws Exception {
    return rest.perform(MockMvcRequestBuilders.get(url));
  }

  public ResultActions postJson(String url, String body) throws Exception {
    return rest.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(body));
  }

  public static String freePlate() {
    var used = USED_PLATES.getAndIncrement();

    return "RC-%03d-%c%c".formatted(used % 1000, (char) ('A' + used / 1000 % 26), (char) ('A' + used / 26000 % 26));
  }

  public static String uniqueEmail() {
    return "driver-" + UUID.randomUUID().toString().substring(0, 8) + "@mail.com";
  }

  public static String read(ResultActions result, String path) throws Exception {
    return JsonPath.read(result.andReturn().getResponse().getContentAsString(), path).toString();
  }
}

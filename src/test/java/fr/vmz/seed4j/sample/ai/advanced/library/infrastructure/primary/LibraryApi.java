package fr.vmz.seed4j.sample.ai.advanced.library.infrastructure.primary;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jayway.jsonpath.JsonPath;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

final class LibraryApi {

  private final MockMvc rest;

  LibraryApi(MockMvc rest) {
    this.rest = rest;
  }

  ResultActions registerMember(String email) throws Exception {
    return postJson("/api/library/members", "{\"fullName\":\"Jane Doe\",\"email\":\"" + email + "\"}");
  }

  String memberId() throws Exception {
    return idOf(registerMember(uniqueEmail()), "$.id");
  }

  ResultActions addBook(String isbn, String title) throws Exception {
    return postJson("/api/library/books", "{\"isbn\":\"%s\",\"title\":\"%s\",\"author\":\"Joshua Bloch\"}".formatted(isbn, title));
  }

  String bookIsbn() throws Exception {
    return idOf(addBook(uniqueIsbn(), "Effective Java"), "$.isbn");
  }

  ResultActions borrow(String memberId, String isbn) throws Exception {
    return postJson("/api/library/loans", "{\"memberId\":\"%s\",\"isbn\":\"%s\"}".formatted(memberId, isbn));
  }

  String loanId(String memberId, String isbn) throws Exception {
    return idOf(borrow(memberId, isbn), "$.id");
  }

  ResultActions returnBook(String loanId) throws Exception {
    return rest.perform(post("/api/library/loans/" + loanId + "/return"));
  }

  ResultActions postJson(String url, String body) throws Exception {
    return rest.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(body));
  }

  static String uniqueEmail() {
    return "member-" + UUID.randomUUID().toString().substring(0, 8) + "@mail.com";
  }

  static String uniqueIsbn() {
    var digits = new StringBuilder("978");
    IntStream.range(0, 9).forEach(index -> digits.append(ThreadLocalRandom.current().nextInt(10)));
    var sum = IntStream.range(0, 12)
      .map(index -> Character.getNumericValue(digits.charAt(index)) * (index % 2 == 0 ? 1 : 3))
      .sum();

    return digits.append((10 - sum % 10) % 10).toString();
  }

  private static String idOf(ResultActions result, String path) throws Exception {
    return JsonPath.read(result.andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), path);
  }
}

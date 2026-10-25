package fr.vmz.seed4j.sample.ai.advanced.library.infrastructure.primary;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.vmz.seed4j.sample.ai.IntegrationTest;
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
class BooksResourceIT {

  // In-memory books are shared by the whole Spring context: each test adds its own books
  @Autowired
  private MockMvc rest;

  private LibraryApi api;

  @BeforeEach
  void loadApi() {
    api = new LibraryApi(rest);
  }

  @Test
  void shouldAddBook() throws Exception {
    var isbn = LibraryApi.uniqueIsbn();

    api
      .addBook(isbn, "Effective Java")
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.isbn").value(isbn))
      .andExpect(jsonPath("$.title").value("Effective Java"))
      .andExpect(jsonPath("$.author").value("Joshua Bloch"))
      .andExpect(jsonPath("$.status").value("AVAILABLE"));
  }

  @Test
  void shouldNotAddTwoBooksWithTheSameIsbn() throws Exception {
    var isbn = api.bookIsbn();

    api.addBook(isbn, "Other").andExpect(status().isConflict());
  }

  @Test
  void shouldNotAddBookWithInvalidIsbn() throws Exception {
    api.addBook("9780134685992", "Effective Java").andExpect(status().isBadRequest()).andExpect(jsonPath("$.title").value("Invalid value"));
  }

  @Test
  void shouldSearchAvailableBooksByTitleAndAuthor() throws Exception {
    var keyword = UUID.randomUUID().toString().substring(0, 8);
    var available = LibraryApi.uniqueIsbn();
    api.addBook(available, "Java " + keyword).andExpect(status().isCreated());
    var inMaintenance = LibraryApi.uniqueIsbn();
    api.addBook(inMaintenance, "Kotlin " + keyword).andExpect(status().isCreated());
    rest.perform(post("/api/library/books/" + inMaintenance + "/maintenance"));

    rest
      .perform(get("/api/library/books/available").param("title", keyword.toUpperCase()).param("author", "bloch"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[*].isbn", contains(available)));
  }

  @Test
  void shouldPutBookInMaintenanceAndBack() throws Exception {
    var isbn = api.bookIsbn();

    rest
      .perform(post("/api/library/books/" + isbn + "/maintenance"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status").value("MAINTENANCE"));
    rest.perform(delete("/api/library/books/" + isbn + "/maintenance")).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("AVAILABLE"));
  }

  @Test
  void shouldNotEndMaintenanceOfAvailableBook() throws Exception {
    rest.perform(delete("/api/library/books/" + api.bookIsbn() + "/maintenance")).andExpect(status().isConflict());
  }

  @Test
  void shouldNotPutUnknownBookInMaintenance() throws Exception {
    rest.perform(post("/api/library/books/" + LibraryApi.uniqueIsbn() + "/maintenance")).andExpect(status().isNotFound());
  }
}

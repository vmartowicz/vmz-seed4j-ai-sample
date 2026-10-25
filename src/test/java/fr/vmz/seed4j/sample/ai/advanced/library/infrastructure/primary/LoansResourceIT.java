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
class LoansResourceIT {

  private static final LocalDate MARCH_10 = LocalDate.of(2026, 3, 10);

  // In-memory data is shared by the whole Spring context: each test registers its own members and books
  @Autowired
  private MockMvc rest;

  @Autowired
  private MovableClock clock;

  private LibraryApi api;

  @BeforeEach
  void loadApi() {
    api = new LibraryApi(rest);
    clock.today(MARCH_10);
  }

  @Test
  void shouldBorrowBookFor14Days() throws Exception {
    var memberId = api.memberId();
    var isbn = api.bookIsbn();

    api
      .borrow(memberId, isbn)
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id").isNotEmpty())
      .andExpect(jsonPath("$.memberId").value(memberId))
      .andExpect(jsonPath("$.isbn").value(isbn))
      .andExpect(jsonPath("$.borrowedOn").value("2026-03-10"))
      .andExpect(jsonPath("$.dueOn").value("2026-03-24"))
      .andExpect(jsonPath("$.returnedOn").doesNotExist());
  }

  @Test
  void shouldNotBorrowBorrowedBook() throws Exception {
    var isbn = api.bookIsbn();
    api.loanId(api.memberId(), isbn);

    api.borrow(api.memberId(), isbn).andExpect(status().isConflict()).andExpect(jsonPath("$.title").value("Business rule violation"));
  }

  @Test
  void shouldNotBorrowMoreThan3Books() throws Exception {
    var memberId = api.memberId();
    for (var index = 0; index < 3; index++) {
      api.loanId(memberId, api.bookIsbn());
    }

    api.borrow(memberId, api.bookIsbn()).andExpect(status().isConflict());
  }

  @Test
  void shouldNotBorrowWithOverdueLoan() throws Exception {
    var memberId = api.memberId();
    api.loanId(memberId, api.bookIsbn());
    clock.today(MARCH_10.plusDays(15));

    api.borrow(memberId, api.bookIsbn()).andExpect(status().isConflict());
  }

  @Test
  void shouldReturnBookLateWithFine() throws Exception {
    var memberId = api.memberId();
    var isbn = api.bookIsbn();
    var loanId = api.loanId(memberId, isbn);
    clock.today(MARCH_10.plusDays(17));

    api
      .returnBook(loanId)
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.returnedOn").value("2026-03-27"))
      .andExpect(jsonPath("$.lateDays").value(3))
      .andExpect(jsonPath("$.fine").value(1.5));
    api.borrow(api.memberId(), isbn).andExpect(status().isCreated());
  }

  @Test
  void shouldSuspendMemberWhoseFinesExceed10Euros() throws Exception {
    var memberId = api.memberId();
    var loanId = api.loanId(memberId, api.bookIsbn());
    clock.today(MARCH_10.plusDays(35));

    api.returnBook(loanId).andExpect(jsonPath("$.fine").value(10.5));

    rest.perform(get("/api/library/members/" + memberId)).andExpect(jsonPath("$.status").value("SUSPENDED"));
    api.borrow(memberId, api.bookIsbn()).andExpect(status().isConflict());
  }

  @Test
  void shouldListFinesOfMember() throws Exception {
    var memberId = api.memberId();
    var isbn = api.bookIsbn();
    var lateLoan = api.loanId(memberId, isbn);
    var onTimeLoan = api.loanId(memberId, api.bookIsbn());
    clock.today(MARCH_10.plusDays(14));
    api.returnBook(onTimeLoan);
    clock.today(MARCH_10.plusDays(16));
    api.returnBook(lateLoan);

    rest
      .perform(get("/api/library/members/" + memberId + "/fines"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.fines", hasSize(1)))
      .andExpect(jsonPath("$.fines[0].loanId").value(lateLoan))
      .andExpect(jsonPath("$.fines[0].isbn").value(isbn))
      .andExpect(jsonPath("$.fines[0].dueOn").value("2026-03-24"))
      .andExpect(jsonPath("$.fines[0].returnedOn").value("2026-03-26"))
      .andExpect(jsonPath("$.fines[0].lateDays").value(2))
      .andExpect(jsonPath("$.fines[0].amount").value(1.0))
      .andExpect(jsonPath("$.total").value(1.0));
  }

  @Test
  void shouldNotReturnBookTwice() throws Exception {
    var loanId = api.loanId(api.memberId(), api.bookIsbn());
    api.returnBook(loanId).andExpect(status().isOk());

    api.returnBook(loanId).andExpect(status().isConflict());
  }

  @Test
  void shouldNotReturnUnknownLoan() throws Exception {
    api.returnBook(UUID.randomUUID().toString()).andExpect(status().isNotFound());
  }

  @Test
  void shouldNotBorrowWithInvalidIsbn() throws Exception {
    api.borrow(api.memberId(), "123").andExpect(status().isBadRequest());
  }
}

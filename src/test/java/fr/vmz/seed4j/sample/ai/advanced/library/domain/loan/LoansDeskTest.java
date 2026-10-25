package fr.vmz.seed4j.sample.ai.advanced.library.domain.loan;

import static fr.vmz.seed4j.sample.ai.advanced.library.domain.book.BooksFixture.*;
import static fr.vmz.seed4j.sample.ai.advanced.library.domain.loan.LoansFixture.*;
import static fr.vmz.seed4j.sample.ai.advanced.library.domain.member.MembersFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.book.Book;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.book.BookNotAvailableException;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.book.BookStatus;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.book.BooksRepository;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.book.UnknownBookException;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.Member;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.MemberId;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.MemberStatus;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.MembersRepository;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.UnknownMemberException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class LoansDeskTest {

  private final MembersRepository members = mock(MembersRepository.class, CALLS_REAL_METHODS);
  private final BooksRepository books = mock(BooksRepository.class, CALLS_REAL_METHODS);
  private final LoansRepository loans = mock(LoansRepository.class, CALLS_REAL_METHODS);
  private final LoansDesk desk = new LoansDesk(members, books, loans);

  private Member jane;
  private Book effectiveJava;

  @BeforeEach
  void loadLibrary() {
    jane = existing(jane());
    effectiveJava = existing(effectiveJava());
    doReturn(List.of()).when(loans).of(any());
  }

  @Nested
  class Borrow {

    @Test
    void shouldBorrowAvailableBook() {
      var loan = desk.borrow(jane.id(), EFFECTIVE_JAVA, TODAY);

      assertThat(loan.member()).isEqualTo(jane.id());
      assertThat(loan.dueOn()).isEqualTo(TODAY.plusDays(14));
      assertThat(effectiveJava.status()).isEqualTo(BookStatus.BORROWED);
      verify(books).save(effectiveJava);
      verify(loans).save(loan);
    }

    @Test
    void shouldNotBorrowUnavailableBook() {
      effectiveJava.startMaintenance();

      assertThatThrownBy(() -> desk.borrow(jane.id(), EFFECTIVE_JAVA, TODAY)).isExactlyInstanceOf(BookNotAvailableException.class);
      verify(loans, never()).save(any());
    }

    @Test
    void shouldNotBorrowWhenSuspended() {
      jane.suspend();

      assertThatThrownBy(() -> desk.borrow(jane.id(), EFFECTIVE_JAVA, TODAY))
        .isExactlyInstanceOf(MemberSuspendedException.class)
        .hasMessageContaining(jane.id().get().toString());
      assertThat(effectiveJava.status()).isEqualTo(BookStatus.AVAILABLE);
    }

    @Test
    void shouldNotBorrowWithOverdueLoan() {
      doReturn(List.of(loan(jane.id(), DDD, TODAY.minusDays(15)))).when(loans).of(jane.id());

      assertThatThrownBy(() -> desk.borrow(jane.id(), EFFECTIVE_JAVA, TODAY)).isExactlyInstanceOf(OverdueLoansException.class);
      assertThat(effectiveJava.status()).isEqualTo(BookStatus.AVAILABLE);
    }

    @Test
    void shouldBorrowWithLoanDueToday() {
      doReturn(List.of(loan(jane.id(), DDD, TODAY.minusDays(14)))).when(loans).of(jane.id());

      assertThat(desk.borrow(jane.id(), EFFECTIVE_JAVA, TODAY).book()).isEqualTo(EFFECTIVE_JAVA);
    }

    @Test
    void shouldNotBorrowMoreThan3Books() {
      doReturn(List.of(ongoing(), ongoing(), ongoing())).when(loans).of(jane.id());

      assertThatThrownBy(() -> desk.borrow(jane.id(), EFFECTIVE_JAVA, TODAY)).isExactlyInstanceOf(BorrowingLimitReachedException.class);
      assertThat(effectiveJava.status()).isEqualTo(BookStatus.AVAILABLE);
    }

    @Test
    void shouldBorrowThirdBookAfterReturns() {
      doReturn(List.of(ongoing(), ongoing(), returnedLoan(jane.id(), DDD, TODAY.minusDays(20), 0))).when(loans).of(jane.id());

      assertThat(desk.borrow(jane.id(), EFFECTIVE_JAVA, TODAY).isOngoing()).isTrue();
    }

    @Test
    void shouldNotBorrowForUnknownMember() {
      var unknown = MemberId.newId();
      doReturn(Optional.empty()).when(members).get(unknown);

      assertThatThrownBy(() -> desk.borrow(unknown, EFFECTIVE_JAVA, TODAY))
        .isExactlyInstanceOf(UnknownMemberException.class)
        .hasMessageContaining(unknown.get().toString());
    }

    @Test
    void shouldNotBorrowUnknownBook() {
      doReturn(Optional.empty()).when(books).get(DDD);

      assertThatThrownBy(() -> desk.borrow(jane.id(), DDD, TODAY)).isExactlyInstanceOf(UnknownBookException.class);
    }

    private Loan ongoing() {
      return loan(jane.id(), DDD, TODAY.minusDays(1));
    }
  }

  @Nested
  class Return {

    @Test
    void shouldReturnBookAndFineLateDays() {
      effectiveJava.borrow();
      var loan = existing(loan(jane.id(), EFFECTIVE_JAVA, TODAY.minusDays(17)));
      doReturn(List.of(loan)).when(loans).of(jane.id());

      var returned = desk.returnBook(loan.id(), TODAY);

      assertThat(returned.fine()).isEqualTo(new Fine(new BigDecimal("1.50")));
      assertThat(effectiveJava.status()).isEqualTo(BookStatus.AVAILABLE);
      assertThat(jane.status()).isEqualTo(MemberStatus.ACTIVE);
      verify(loans).save(loan);
      verify(books).save(effectiveJava);
    }

    @Test
    void shouldSuspendMemberWhoseFinesExceed10Euros() {
      effectiveJava.borrow();
      var loan = existing(loan(jane.id(), EFFECTIVE_JAVA, TODAY.minusDays(16)));
      doReturn(List.of(returnedLoan(jane.id(), DDD, TODAY.minusDays(60), 19), loan)).when(loans).of(jane.id());

      desk.returnBook(loan.id(), TODAY);

      assertThat(jane.status()).isEqualTo(MemberStatus.SUSPENDED);
      verify(members).save(jane);
    }

    @Test
    void shouldNotSuspendMemberWhoseFinesReach10Euros() {
      effectiveJava.borrow();
      var loan = existing(loan(jane.id(), EFFECTIVE_JAVA, TODAY.minusDays(16)));
      doReturn(List.of(returnedLoan(jane.id(), DDD, TODAY.minusDays(60), 18), loan)).when(loans).of(jane.id());

      desk.returnBook(loan.id(), TODAY);

      assertThat(jane.status()).isEqualTo(MemberStatus.ACTIVE);
      verify(members, never()).save(any());
    }

    @Test
    void shouldNotReturnBookTwice() {
      var loan = existing(returnedLoan(jane.id(), EFFECTIVE_JAVA, TODAY.minusDays(20), 0));

      assertThatThrownBy(() -> desk.returnBook(loan.id(), TODAY)).isExactlyInstanceOf(LoanAlreadyReturnedException.class);
      verify(books, never()).save(any());
    }

    @Test
    void shouldNotReturnUnknownLoan() {
      var unknown = LoanId.newId();
      doReturn(Optional.empty()).when(loans).get(unknown);

      assertThatThrownBy(() -> desk.returnBook(unknown, TODAY))
        .isExactlyInstanceOf(UnknownLoanException.class)
        .hasMessageContaining(unknown.get().toString());
    }
  }

  @Test
  void shouldListFinesOfLateReturns() {
    var latest = returnedLoan(jane.id(), EFFECTIVE_JAVA, TODAY.minusDays(30), 2);
    var oldest = returnedLoan(jane.id(), DDD, TODAY.minusDays(60), 4);
    var onTime = returnedLoan(jane.id(), DDD, TODAY.minusDays(40), 0);
    var ongoing = loan(jane.id(), DDD, TODAY.minusDays(20));
    doReturn(new ArrayList<>(List.of(latest, oldest, onTime, ongoing))).when(loans).of(jane.id());

    var fines = desk.finesOf(jane.id());

    assertThat(fines.lateReturns()).containsExactly(oldest, latest);
    assertThat(fines.total()).isEqualTo(new Fine(new BigDecimal("3.00")));
  }

  private Member existing(Member member) {
    doReturn(Optional.of(member)).when(members).get(member.id());

    return member;
  }

  private Book existing(Book book) {
    doReturn(Optional.of(book)).when(books).get(book.isbn());

    return book;
  }

  private Loan existing(Loan loan) {
    doReturn(Optional.of(loan)).when(loans).get(loan.id());

    return loan;
  }
}

package fr.vmz.seed4j.sample.ai.advanced.library.domain.loan;

import static fr.vmz.seed4j.sample.ai.advanced.library.domain.book.BooksFixture.*;
import static fr.vmz.seed4j.sample.ai.advanced.library.domain.loan.LoansFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.MemberId;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

@UnitTest
class LoanTest {

  private static final MemberId JANE = MemberId.newId();

  @Test
  void shouldBeDue14DaysAfterBorrowing() {
    var loan = loan(JANE, EFFECTIVE_JAVA, TODAY);

    assertThat(loan.id()).isNotNull();
    assertThat(loan.member()).isEqualTo(JANE);
    assertThat(loan.book()).isEqualTo(EFFECTIVE_JAVA);
    assertThat(loan.borrowedOn()).isEqualTo(TODAY);
    assertThat(loan.dueOn()).isEqualTo(TODAY.plusDays(14));
    assertThat(loan.returnedOn()).isEmpty();
    assertThat(loan.isOngoing()).isTrue();
  }

  @Test
  void shouldNotBeOverdueOnItsDueDate() {
    assertThat(loan(JANE, EFFECTIVE_JAVA, TODAY).isOverdueOn(TODAY.plusDays(14))).isFalse();
  }

  @Test
  void shouldBeOverdueTheDayAfterItsDueDate() {
    assertThat(loan(JANE, EFFECTIVE_JAVA, TODAY).isOverdueOn(TODAY.plusDays(15))).isTrue();
  }

  @Test
  void shouldNotBeOverdueOnceReturned() {
    var loan = returnedLoan(JANE, EFFECTIVE_JAVA, TODAY, 3);

    assertThat(loan.isOngoing()).isFalse();
    assertThat(loan.isOverdueOn(TODAY.plusDays(30))).isFalse();
  }

  @Test
  void shouldNotFineReturnOnDueDate() {
    var loan = loan(JANE, EFFECTIVE_JAVA, TODAY);

    var fine = loan.giveBack(TODAY.plusDays(14));

    assertThat(fine).isEqualTo(Fine.NONE);
    assertThat(loan.returnedOn()).contains(TODAY.plusDays(14));
    assertThat(loan.lateDays()).isZero();
  }

  @Test
  void shouldNotFineEarlyReturn() {
    assertThat(loan(JANE, EFFECTIVE_JAVA, TODAY).giveBack(TODAY.plusDays(2))).isEqualTo(Fine.NONE);
  }

  @Test
  void shouldFine50CentsPerLateDay() {
    var loan = loan(JANE, EFFECTIVE_JAVA, TODAY);

    var fine = loan.giveBack(TODAY.plusDays(17));

    assertThat(loan.lateDays()).isEqualTo(3);
    assertThat(fine).isEqualTo(new Fine(new BigDecimal("1.50")));
    assertThat(loan.fine()).isEqualTo(fine);
  }

  @Test
  void shouldNotFineOngoingLoan() {
    assertThat(loan(JANE, EFFECTIVE_JAVA, TODAY).fine()).isEqualTo(Fine.NONE);
  }

  @Test
  void shouldNotReturnLoanTwice() {
    var loan = returnedLoan(JANE, EFFECTIVE_JAVA, TODAY, 0);

    assertThatThrownBy(() -> loan.giveBack(TODAY.plusDays(20)))
      .isExactlyInstanceOf(LoanAlreadyReturnedException.class)
      .hasMessageContaining(loan.id().get().toString());
  }
}

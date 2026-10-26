package fr.vmz.seed4j.sample.ai.expert.bank.lending.domain;

import static fr.vmz.seed4j.sample.ai.expert.bank.lending.domain.LendingFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class LoanOfficeTest {

  private final LoansRepository loans = mock(LoansRepository.class, CALLS_REAL_METHODS);
  private final Borrowers borrowers = mock(Borrowers.class);
  private final LoanAccounts accounts = mock(LoanAccounts.class);
  private final LoanOffice office = new LoanOffice(loans, borrowers, accounts);

  private Borrower borrower;

  @BeforeEach
  void loadBorrower() {
    borrower = known(borrower("3000"));
    doReturn(List.of()).when(loans).all();
  }

  @Nested
  class Application {

    @Test
    void shouldSignLoanOnCurrentAccount() {
      var loan = office.apply(request(borrower, "10000", 24), TODAY);

      assertThat(loan.account()).isEqualTo(CURRENT_ACCOUNT);
      verify(loans).save(loan);
    }

    @Test
    void shouldNotLendToCustomersOfLessThan6Months() {
      var newcomer = known(new Borrower(CustomerId.newId(), LocalDate.of(1980, 1, 1), new BigDecimal("3000"), TODAY.minusMonths(6).plusDays(1)));

      assertRefused(request(newcomer, "10000", 24), "6 months");
    }

    @Test
    void shouldNotLendBeyond75YearsOld() {
      var senior = known(new Borrower(CustomerId.newId(), TODAY.minusYears(73), new BigDecimal("3000"), TODAY.minusYears(10)));

      assertThat(office.apply(request(senior, "10000", 23), TODAY).months()).isEqualTo(23);
      clearInvocations(loans);
      assertRefused(request(senior, "10000", 24), "75");
    }

    @Test
    void shouldNotLendAfterUnauthorizedOverdraft() {
      doReturn(true).when(accounts).wasInUnauthorizedOverdraftSince(borrower.id(), TODAY.minusMonths(3));

      assertRefused(request(borrower, "10000", 24), "overdraft");
    }

    @Test
    void shouldNotLendWithoutCurrentAccount() {
      doReturn(Optional.empty()).when(accounts).currentAccountOf(borrower.id());

      assertRefused(request(borrower, "10000", 24), "current account");
    }

    @Test
    void shouldNotExceed35PercentDebtRatioWithRunningLoans() {
      var running = Loan.sign(request(borrower, "10000", 24), CURRENT_ACCOUNT, TODAY.minusMonths(3));
      var repaid = Loan.sign(request(borrower, "1000", 12), CURRENT_ACCOUNT, TODAY.minusYears(2));
      repaid.disburseIfDue(TODAY.minusYears(2).plusDays(14));
      for (int month = 0; month < 12; month++) {
        repaid.instalmentPaid();
      }
      var otherBorrower = Loan.sign(request(borrower("3000"), "75000", 84), CURRENT_ACCOUNT, TODAY);
      doReturn(List.of(running, repaid, otherBorrower)).when(loans).all();

      assertThat(office.apply(request(borrower, "13000", 24), TODAY).instalment()).isEqualByComparingTo("573.58");
      clearInvocations(loans);
      assertRefused(request(borrower, "15000", 24), "35%");
    }

    @Test
    void shouldNotLendToUnknownBorrower() {
      var unknown = CustomerId.newId();
      doReturn(Optional.empty()).when(borrowers).borrower(unknown);

      assertThatThrownBy(() -> office.apply(new LoanRequest(unknown, new BigDecimal("10000"), 24), TODAY)).isExactlyInstanceOf(UnknownBorrowerException.class);
    }

    private void assertRefused(LoanRequest request, String reason) {
      assertThatThrownBy(() -> office.apply(request, TODAY)).isExactlyInstanceOf(LoanRefusedException.class).hasMessageContaining(reason);
      verify(loans, never()).save(any());
    }
  }

  @Nested
  class Lifecycle {

    @Test
    void shouldDisburseLoansAfterCoolingOff() {
      var due = signed();
      var recent = Loan.sign(request(borrower, "5000", 24), CURRENT_ACCOUNT, TODAY.plusDays(5));
      doReturn(List.of(due, recent)).when(loans).all();

      office.disburse(TODAY.plusDays(14));

      verify(accounts).credit(CURRENT_ACCOUNT, new BigDecimal("10000"), "Personal loan " + due.id().get());
      verify(loans).save(due);
      verify(loans, never()).save(recent);
    }

    @Test
    void shouldCollectDueInstalments() {
      var paying = active();
      var failing = active();
      var notDue = signed();
      doReturn(List.of(paying, failing, notDue)).when(loans).all();
      doReturn(true, false).when(accounts).collect(any(), any(), anyString());

      office.collectInstalments(TODAY.plusDays(14).plusMonths(1));

      assertThat(paying.paidInstalments()).isEqualTo(1);
      assertThat(failing.unpaidInstalments()).isEqualTo(1);
      verify(loans).save(paying);
      verify(loans).save(failing);
      verify(loans, never()).save(notDue);
    }

    @Test
    void shouldNotFindUnknownLoan() {
      var unknown = LoanId.newId();
      doReturn(Optional.empty()).when(loans).get(unknown);

      assertThatThrownBy(() -> office.loan(unknown)).isExactlyInstanceOf(UnknownLoanException.class);
    }
  }

  private Borrower known(Borrower known) {
    doReturn(Optional.of(known)).when(borrowers).borrower(known.id());
    doReturn(Optional.of(CURRENT_ACCOUNT)).when(accounts).currentAccountOf(known.id());

    return known;
  }
}

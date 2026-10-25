package fr.vmz.seed4j.sample.ai.expert.gym.billing.domain;

import static fr.vmz.seed4j.sample.ai.expert.gym.billing.domain.BillingFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class BillingDeskTest {

  private final BillingAccountsRepository accounts = mock(BillingAccountsRepository.class, CALLS_REAL_METHODS);
  private final InvoicesRepository invoices = mock(InvoicesRepository.class, CALLS_REAL_METHODS);
  private final Bank bank = mock(Bank.class);
  private final MembershipLifecycle memberships = mock(MembershipLifecycle.class);
  private final BillingDesk desk = new BillingDesk(accounts, invoices, bank, memberships);

  private final MemberId member = MemberId.newId();
  private final List<Invoice> memberInvoices = new ArrayList<>();
  private BillingAccount account;

  @BeforeEach
  void loadAccount() {
    account = BillingAccount.open(member, euros("29.90"));
    doReturn(Optional.of(account)).when(accounts).get(member);
    doReturn(List.of(account)).when(accounts).all();
    doReturn(memberInvoices).when(invoices).of(member);
    doReturn(List.of()).when(invoices).all();
  }

  @Test
  void shouldOpenAccountWithFirstMonthInvoice() {
    var invoice = desk.openAccount(member, euros("29.90"), euros("39"), SUBSCRIPTION_DAY);

    assertThat(invoice.amount()).isEqualTo(euros("60.22"));
    verify(accounts).save(argThat(saved -> saved.member().equals(member) && saved.monthlyFee().equals(euros("29.90"))));
    verify(invoices).save(invoice);
  }

  @Nested
  class CardPayment {

    @Test
    void shouldActivateMembershipOnFirstPayment() {
      var invoice = known(Invoice.firstMonth(member, euros("29.90"), euros("39"), SUBSCRIPTION_DAY));

      desk.payByCard(invoice.id());

      assertThat(invoice.isPaid()).isTrue();
      assertThat(account.status()).isEqualTo(AccountStatus.UP_TO_DATE);
      verify(memberships).activate(member);
      verify(accounts).save(account);
      verify(invoices).save(invoice);
    }

    @Test
    void shouldNotActivateMembershipForOtherInvoices() {
      var invoice = known(Invoice.arrearsFee(member, DEBIT_DAY));

      desk.payByCard(invoice.id());

      assertThat(invoice.isPaid()).isTrue();
      verifyNoInteractions(memberships);
    }

    @Test
    void shouldNotPayUnknownInvoice() {
      var unknown = InvoiceId.newId();
      doReturn(Optional.empty()).when(invoices).get(unknown);

      assertThatThrownBy(() -> desk.payByCard(unknown)).isExactlyInstanceOf(UnknownInvoiceException.class);
    }
  }

  @Nested
  class DebitResult {

    @Test
    void shouldPayInvoiceWhenDebitIsAccepted() {
      var invoice = known(Invoice.monthly(member, euros("29.90"), APRIL, DEBIT_DAY));

      desk.debitResult(invoice.id(), true, DEBIT_DAY);

      assertThat(invoice.isPaid()).isTrue();
      verify(invoices).save(invoice);
    }

    @Test
    void shouldScheduleRetryWhenDebitIsRejected() {
      var invoice = known(Invoice.monthly(member, euros("29.90"), APRIL, DEBIT_DAY));

      desk.debitResult(invoice.id(), false, DEBIT_DAY);

      assertThat(invoice.status()).isEqualTo(InvoiceStatus.DEBIT_RETRY_SCHEDULED);
      assertThat(account.isInArrears()).isFalse();
    }

    @Test
    void shouldPutAccountInArrearsWithFeeAfterThirdRejection() {
      account.firstPaymentReceived();
      var unpaid = known(rejectedTimes(member, 2));

      desk.debitResult(unpaid.id(), false, LocalDate.of(2026, 4, 15));

      assertThat(account.isInArrears()).isTrue();
      verify(accounts).save(account);
      verify(invoices).save(argThat(fee -> fee.kind() == InvoiceKind.ARREARS_FEE && fee.member().equals(member)));
    }
  }

  @Nested
  class Settlement {

    @Test
    void shouldPayEverythingDueByCard() {
      account.firstPaymentReceived();
      account.fallIntoArrears(DEBIT_DAY);
      var unpaid = rejectedTimes(member, 3);
      var fee = Invoice.arrearsFee(member, DEBIT_DAY);
      var retrying = Invoice.monthly(member, euros("29.90"), APRIL.plusMonths(1), DEBIT_DAY.plusMonths(1));
      memberInvoices.addAll(List.of(unpaid, fee, retrying));

      var statement = desk.settle(member);

      assertThat(statement.account().status()).isEqualTo(AccountStatus.UP_TO_DATE);
      assertThat(unpaid.isPaid()).isTrue();
      assertThat(fee.isPaid()).isTrue();
      assertThat(retrying.isPaid()).isFalse();
      verify(invoices).save(unpaid);
      verify(invoices).save(fee);
      verify(invoices, never()).save(retrying);
    }

    @Test
    void shouldNotSettleUnknownAccount() {
      var unknown = MemberId.newId();
      doReturn(Optional.empty()).when(accounts).get(unknown);

      assertThatThrownBy(() -> desk.settle(unknown)).isExactlyInstanceOf(UnknownBillingAccountException.class);
    }
  }

  @Nested
  class Daily {

    @Test
    void shouldDebitBillableAccountsOnThe5th() {
      account.firstPaymentReceived();

      desk.runDaily(DEBIT_DAY);

      verify(invoices).save(argThat(invoice -> invoice.kind() == InvoiceKind.MONTHLY && invoice.period().equals(APRIL)));
      verify(bank).submitDebit(argThat(invoice -> invoice.amount().equals(euros("29.90"))));
    }

    @Test
    void shouldNotDebitOtherDays() {
      account.firstPaymentReceived();

      desk.runDaily(DEBIT_DAY.plusDays(1));

      verifyNoInteractions(bank);
    }

    @Test
    void shouldNotDebitAccountsAwaitingFirstPayment() {
      desk.runDaily(DEBIT_DAY);

      verifyNoInteractions(bank);
    }

    @Test
    void shouldNotDebitSuspendedMembers() {
      account.firstPaymentReceived();
      doReturn(true).when(memberships).isSuspendedOn(member, DEBIT_DAY);

      desk.runDaily(DEBIT_DAY);

      verifyNoInteractions(bank);
    }

    @Test
    void shouldNotDebitTwiceTheSameMonth() {
      account.firstPaymentReceived();
      memberInvoices.add(Invoice.firstMonth(member, euros("29.90"), euros("39"), LocalDate.of(2026, 4, 2)));
      memberInvoices.add(Invoice.arrearsFee(member, LocalDate.of(2026, 3, 15)));

      desk.runDaily(DEBIT_DAY);

      verifyNoInteractions(bank);
    }

    @Test
    void shouldDebitMonthDespiteArrearsFeeOfThatMonth() {
      account.firstPaymentReceived();
      memberInvoices.add(Invoice.firstMonth(member, euros("29.90"), euros("39"), SUBSCRIPTION_DAY));
      memberInvoices.add(Invoice.arrearsFee(member, LocalDate.of(2026, 4, 1)));

      desk.runDaily(DEBIT_DAY);

      verify(bank).submitDebit(any());
    }

    @Test
    void shouldSubmitDueRetries() {
      var due = rejectedOn(DEBIT_DAY);
      var later = rejectedOn(DEBIT_DAY.plusDays(1));
      doReturn(List.of(due, later)).when(invoices).all();

      desk.runDaily(LocalDate.of(2026, 4, 8));

      verify(bank).submitDebit(due);
      verify(invoices).save(due);
      assertThat(later.attempts()).isEqualTo(1);
      verify(bank, never()).submitDebit(later);
    }

    @Test
    void shouldTerminateMembershipsAfter2MonthsOfArrears() {
      account.firstPaymentReceived();
      account.fallIntoArrears(LocalDate.of(2026, 4, 15));

      desk.runDaily(LocalDate.of(2026, 6, 15));

      assertThat(account.status()).isEqualTo(AccountStatus.CLOSED);
      verify(memberships).terminate(member);
      verify(accounts).save(account);
    }

    @Test
    void shouldKeepMembershipsWithRecentArrears() {
      account.firstPaymentReceived();
      account.fallIntoArrears(LocalDate.of(2026, 4, 15));

      desk.runDaily(LocalDate.of(2026, 6, 14));

      verify(memberships, never()).terminate(any());
    }
  }

  @Test
  void shouldGiveStatementWithInvoicesInIssueOrder() {
    var april = Invoice.monthly(member, euros("29.90"), APRIL, DEBIT_DAY);
    var first = Invoice.firstMonth(member, euros("29.90"), euros("39"), SUBSCRIPTION_DAY);
    var fee = Invoice.arrearsFee(member, DEBIT_DAY);
    memberInvoices.addAll(List.of(fee, april, first));

    var statement = desk.statement(member);

    assertThat(statement.account()).isSameAs(account);
    assertThat(statement.invoices()).containsExactly(first, april, fee);
  }

  @Test
  void shouldKnowArrears() {
    var unknown = MemberId.newId();
    doReturn(Optional.empty()).when(accounts).get(unknown);
    account.firstPaymentReceived();
    account.fallIntoArrears(DEBIT_DAY);

    assertThat(desk.isInArrears(member)).isTrue();
    assertThat(desk.isInArrears(unknown)).isFalse();
  }

  private Invoice known(Invoice invoice) {
    doReturn(Optional.of(invoice)).when(invoices).get(invoice.id());

    return invoice;
  }

  private static Invoice rejectedOn(LocalDate day) {
    var invoice = monthlyInvoice();
    invoice.debitRejected(day);

    return invoice;
  }
}

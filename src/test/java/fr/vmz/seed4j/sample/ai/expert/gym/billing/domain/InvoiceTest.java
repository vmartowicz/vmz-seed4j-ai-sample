package fr.vmz.seed4j.sample.ai.expert.gym.billing.domain;

import static fr.vmz.seed4j.sample.ai.expert.gym.billing.domain.BillingFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.time.LocalDate;
import java.time.YearMonth;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class InvoiceTest {

  @Nested
  class FirstMonth {

    @Test
    void shouldBillRemainingDaysAndSignUpFee() {
      var member = MemberId.newId();

      var invoice = Invoice.firstMonth(member, euros("29.90"), euros("39"), SUBSCRIPTION_DAY);

      assertThat(invoice.id()).isNotNull();
      assertThat(invoice.member()).isEqualTo(member);
      assertThat(invoice.kind()).isEqualTo(InvoiceKind.FIRST_MONTH);
      assertThat(invoice.period()).isEqualTo(YearMonth.of(2026, 3));
      assertThat(invoice.amount()).isEqualTo(euros("60.22"));
      assertThat(invoice.status()).isEqualTo(InvoiceStatus.AWAITING_PAYMENT);
      assertThat(invoice.isPaid()).isFalse();
    }

    @Test
    void shouldBillWholeMonthWhenSubscribingOnTheFirst() {
      var invoice = Invoice.firstMonth(MemberId.newId(), euros("29.90"), euros("0"), LocalDate.of(2026, 2, 1));

      assertThat(invoice.amount()).isEqualTo(euros("29.90"));
    }

    @Test
    void shouldBillOneDayWhenSubscribingOnLastDay() {
      var invoice = Invoice.firstMonth(MemberId.newId(), euros("31.00"), euros("0"), LocalDate.of(2026, 3, 31));

      assertThat(invoice.amount()).isEqualTo(euros("1.00"));
    }

    @Test
    void shouldBePaidByCard() {
      var invoice = Invoice.firstMonth(MemberId.newId(), euros("29.90"), euros("39"), SUBSCRIPTION_DAY);

      invoice.payByCard();

      assertThat(invoice.status()).isEqualTo(InvoiceStatus.PAID);
      assertThat(invoice.isPaid()).isTrue();
    }

    @Test
    void shouldNotBePaidTwice() {
      var invoice = Invoice.firstMonth(MemberId.newId(), euros("29.90"), euros("39"), SUBSCRIPTION_DAY);
      invoice.payByCard();

      assertThatThrownBy(invoice::payByCard).isExactlyInstanceOf(InvoiceAlreadyPaidException.class);
    }
  }

  @Nested
  class DirectDebit {

    @Test
    void shouldSubmitFirstDebitOfMonthlyInvoice() {
      var invoice = monthlyInvoice();

      assertThat(invoice.kind()).isEqualTo(InvoiceKind.MONTHLY);
      assertThat(invoice.amount()).isEqualTo(euros("29.90"));
      assertThat(invoice.status()).isEqualTo(InvoiceStatus.DEBIT_SUBMITTED);
      assertThat(invoice.attempts()).isEqualTo(1);
      assertThat(invoice.nextAttemptOn()).isEmpty();
    }

    @Test
    void shouldBePaidWhenBankAcceptsDebit() {
      var invoice = monthlyInvoice();

      invoice.debitAccepted();

      assertThat(invoice.status()).isEqualTo(InvoiceStatus.PAID);
    }

    @Test
    void shouldRetry3DaysAfterFirstRejection() {
      var invoice = monthlyInvoice();

      assertThat(invoice.debitRejected(DEBIT_DAY)).isFalse();

      assertThat(invoice.status()).isEqualTo(InvoiceStatus.DEBIT_RETRY_SCHEDULED);
      assertThat(invoice.nextAttemptOn()).contains(LocalDate.of(2026, 4, 8));
    }

    @Test
    void shouldNotRetryBeforeScheduledDay() {
      var invoice = monthlyInvoice();
      invoice.debitRejected(DEBIT_DAY);

      assertThat(invoice.retryIfDue(LocalDate.of(2026, 4, 7))).isFalse();
      assertThat(invoice.status()).isEqualTo(InvoiceStatus.DEBIT_RETRY_SCHEDULED);
    }

    @Test
    void shouldSubmitRetryOnScheduledDay() {
      var invoice = monthlyInvoice();
      invoice.debitRejected(DEBIT_DAY);

      assertThat(invoice.retryIfDue(LocalDate.of(2026, 4, 8))).isTrue();

      assertThat(invoice.status()).isEqualTo(InvoiceStatus.DEBIT_SUBMITTED);
      assertThat(invoice.attempts()).isEqualTo(2);
      assertThat(invoice.nextAttemptOn()).isEmpty();
    }

    @Test
    void shouldNotRetrySubmittedDebit() {
      assertThat(monthlyInvoice().retryIfDue(DEBIT_DAY.plusDays(10))).isFalse();
    }

    @Test
    void shouldRetry7DaysAfterSecondRejection() {
      var invoice = rejectedTimes(1);

      assertThat(invoice.debitRejected(LocalDate.of(2026, 4, 8))).isFalse();

      assertThat(invoice.nextAttemptOn()).contains(LocalDate.of(2026, 4, 15));
    }

    @Test
    void shouldBeUnpaidAfterThirdRejection() {
      var invoice = rejectedTimes(2);
      assertThat(invoice.attempts()).isEqualTo(3);

      assertThat(invoice.debitRejected(LocalDate.of(2026, 4, 15))).isTrue();

      assertThat(invoice.status()).isEqualTo(InvoiceStatus.UNPAID);
      assertThat(invoice.nextAttemptOn()).isEmpty();
    }

    @Test
    void shouldNotAcceptResultWithoutDebitInProgress() {
      var invoice = monthlyInvoice();
      invoice.debitAccepted();

      assertThatThrownBy(invoice::debitAccepted).isExactlyInstanceOf(NoDebitInProgressException.class);
      assertThatThrownBy(() -> invoice.debitRejected(DEBIT_DAY)).isExactlyInstanceOf(NoDebitInProgressException.class);
    }

    @Test
    void shouldPayUnpaidInvoiceByCard() {
      var invoice = rejectedTimes(3);

      invoice.payByCard();

      assertThat(invoice.isPaid()).isTrue();
    }
  }

  @Test
  void shouldBill10EurosOfArrearsFee() {
    var invoice = Invoice.arrearsFee(MemberId.newId(), LocalDate.of(2026, 4, 15));

    assertThat(invoice.kind()).isEqualTo(InvoiceKind.ARREARS_FEE);
    assertThat(invoice.period()).isEqualTo(APRIL);
    assertThat(invoice.amount()).isEqualTo(euros("10"));
    assertThat(invoice.status()).isEqualTo(InvoiceStatus.AWAITING_PAYMENT);
  }
}

package fr.vmz.seed4j.sample.ai.expert.bank.lending.domain;

import static fr.vmz.seed4j.sample.ai.expert.bank.lending.domain.LendingFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@UnitTest
class LoanTest {

  @Test
  void shouldComputeInstalmentWith5PercentRateAndInsurance() {
    assertThat(LoanTerms.monthlyInstalment(new BigDecimal("10000"), 24)).isEqualByComparingTo("441.21");
    assertThat(LoanTerms.monthlyInstalment(new BigDecimal("75000"), 84)).isEqualByComparingTo("1078.79");
  }

  @ParameterizedTest
  @CsvSource({ "999.99, 24", "75000.01, 24", "10000, 11", "10000, 85" })
  void shouldLendFrom1000To75000Over12To84Months(String amount, int months) {
    assertThatThrownBy(() -> new LoanRequest(CustomerId.newId(), new BigDecimal(amount), months)).isInstanceOf(AssertionException.class);
  }

  @Test
  void shouldSignLoanWithCoolingOffPeriod() {
    var loan = signed();

    assertThat(loan.id()).isNotNull();
    assertThat(loan.amount()).isEqualByComparingTo("10000");
    assertThat(loan.months()).isEqualTo(24);
    assertThat(loan.instalment()).isEqualByComparingTo("441.21");
    assertThat(loan.account()).isEqualTo(CURRENT_ACCOUNT);
    assertThat(loan.signedOn()).isEqualTo(TODAY);
    assertThat(loan.status()).isEqualTo(LoanStatus.SIGNED);
    assertThat(loan.isRunning()).isTrue();
    assertThat(loan.disburseIfDue(TODAY.plusDays(13))).isFalse();
    assertThat(loan.nextInstalmentOn()).isEmpty();
  }

  @Test
  void shouldDisburseAfter14Days() {
    var loan = signed();

    assertThat(loan.disburseIfDue(TODAY.plusDays(14))).isTrue();

    assertThat(loan.status()).isEqualTo(LoanStatus.ACTIVE);
    assertThat(loan.nextInstalmentOn()).contains(TODAY.plusDays(14).plusMonths(1));
    assertThat(loan.disburseIfDue(TODAY.plusDays(15))).isFalse();
  }

  @Test
  void shouldCollectInstalmentsMonthly() {
    var loan = active();
    var firstDue = TODAY.plusDays(14).plusMonths(1);

    assertThat(loan.isInstalmentDueOn(firstDue.minusDays(1))).isFalse();
    assertThat(loan.isInstalmentDueOn(firstDue)).isTrue();
    loan.instalmentPaid();

    assertThat(loan.nextInstalmentOn()).contains(firstDue.plusMonths(1));
    assertThat(signed().isInstalmentDueOn(firstDue)).isFalse();
  }

  @Test
  void shouldBeRepaidAfterLastInstalment() {
    var loan = Loan.sign(request(borrower("3000"), "1000", 12), CURRENT_ACCOUNT, TODAY);
    loan.disburseIfDue(TODAY.plusDays(14));

    for (int month = 0; month < 12; month++) {
      loan.instalmentPaid();
    }

    assertThat(loan.status()).isEqualTo(LoanStatus.REPAID);
    assertThat(loan.isRunning()).isFalse();
  }

  @Test
  void shouldGoToCollectionAfterTwoUnpaidInstalments() {
    var loan = active();

    loan.instalmentUnpaid();
    assertThat(loan.status()).isEqualTo(LoanStatus.ACTIVE);
    loan.instalmentUnpaid();

    assertThat(loan.status()).isEqualTo(LoanStatus.IN_COLLECTION);
    assertThat(loan.unpaidInstalments()).isEqualTo(2);
    assertThat(loan.isInstalmentDueOn(TODAY.plusYears(1))).isFalse();
  }
}

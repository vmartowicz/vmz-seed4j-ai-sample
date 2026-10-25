package fr.vmz.seed4j.sample.ai.expert.gym.billing.domain;

import static fr.vmz.seed4j.sample.ai.expert.gym.billing.domain.BillingFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

@UnitTest
class BillingAccountTest {

  private static final LocalDate UNPAID_DAY = LocalDate.of(2026, 4, 15);

  @Test
  void shouldWaitForFirstPayment() {
    var member = MemberId.newId();

    var account = BillingAccount.open(member, euros("29.90"));

    assertThat(account.member()).isEqualTo(member);
    assertThat(account.monthlyFee()).isEqualTo(euros("29.90"));
    assertThat(account.status()).isEqualTo(AccountStatus.AWAITING_FIRST_PAYMENT);
    assertThat(account.isBillable()).isFalse();
    assertThat(account.isInArrears()).isFalse();
  }

  @Test
  void shouldBeBillableOnceFirstPaymentReceived() {
    assertThat(upToDate().status()).isEqualTo(AccountStatus.UP_TO_DATE);
    assertThat(upToDate().isBillable()).isTrue();
  }

  @Test
  void shouldFallIntoArrears() {
    var account = upToDate();

    account.fallIntoArrears(UNPAID_DAY);

    assertThat(account.isInArrears()).isTrue();
    assertThat(account.isBillable()).isTrue();
    assertThat(account.arrearsSince()).contains(UNPAID_DAY);
  }

  @Test
  void shouldKeepFirstArrearsDay() {
    var account = upToDate();
    account.fallIntoArrears(UNPAID_DAY);

    account.fallIntoArrears(UNPAID_DAY.plusMonths(1));

    assertThat(account.arrearsSince()).contains(UNPAID_DAY);
  }

  @Test
  void shouldBeUpToDateOnceSettled() {
    var account = upToDate();
    account.fallIntoArrears(UNPAID_DAY);

    account.settle();

    assertThat(account.status()).isEqualTo(AccountStatus.UP_TO_DATE);
    assertThat(account.arrearsSince()).isEmpty();
  }

  @Test
  void shouldNotSettleAccountWithoutArrears() {
    assertThatThrownBy(() -> upToDate().settle()).isExactlyInstanceOf(NotInArrearsException.class);
  }

  @Test
  void shouldBeTerminatedAfter2MonthsOfArrears() {
    var account = upToDate();
    account.fallIntoArrears(UNPAID_DAY);

    assertThat(account.shouldBeTerminatedOn(UNPAID_DAY.plusMonths(2).minusDays(1))).isFalse();
    assertThat(account.shouldBeTerminatedOn(UNPAID_DAY.plusMonths(2))).isTrue();
    assertThat(upToDate().shouldBeTerminatedOn(UNPAID_DAY.plusYears(1))).isFalse();
  }

  @Test
  void shouldNotBeBilledOnceClosed() {
    var account = upToDate();
    account.fallIntoArrears(UNPAID_DAY);

    account.close();

    assertThat(account.status()).isEqualTo(AccountStatus.CLOSED);
    assertThat(account.isBillable()).isFalse();
    assertThat(account.shouldBeTerminatedOn(UNPAID_DAY.plusYears(1))).isFalse();
  }

  private static BillingAccount upToDate() {
    var account = BillingAccount.open(MemberId.newId(), euros("29.90"));
    account.firstPaymentReceived();

    return account;
  }
}

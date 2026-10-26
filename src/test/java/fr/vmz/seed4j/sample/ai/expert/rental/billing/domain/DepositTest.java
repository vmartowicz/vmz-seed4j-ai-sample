package fr.vmz.seed4j.sample.ai.expert.rental.billing.domain;

import static fr.vmz.seed4j.sample.ai.expert.rental.billing.domain.RentalBillingFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

@UnitTest
class DepositTest {

  @Test
  void shouldHoldDepositDuringRental() {
    var deposit = held();

    assertThat(deposit.customer()).isEqualTo(JANE);
    assertThat(deposit.amount()).isEqualByComparingTo("800");
    assertThat(deposit.status()).isEqualTo(DepositStatus.HELD);
    assertThat(deposit.returnedOn()).isEmpty();
    assertThat(deposit.releaseIfDue(RETURN_DAY.plusYears(1))).isFalse();
  }

  @Test
  void shouldReleaseDeposit7DaysAfterReturnWithoutDamage() {
    var deposit = returned(false);

    assertThat(deposit.releaseIfDue(RETURN_DAY.plusDays(6))).isFalse();
    assertThat(deposit.releaseIfDue(RETURN_DAY.plusDays(7))).isTrue();

    assertThat(deposit.status()).isEqualTo(DepositStatus.RELEASED);
    assertThat(deposit.released()).isEqualByComparingTo("800");
    assertThat(deposit.releaseIfDue(RETURN_DAY.plusDays(8))).isFalse();
  }

  @Test
  void shouldKeepDepositOfDamagedVehicleUntilQuote() {
    var deposit = returned(true);

    assertThat(deposit.releaseIfDue(RETURN_DAY.plusDays(30))).isFalse();
    assertThat(deposit.releaseIfDue(RETURN_DAY.plusDays(31))).isTrue();

    assertThat(deposit.retained()).isEqualByComparingTo("0");
  }

  @Test
  void shouldRetainDamageQuoteWithinTheExcess() {
    var small = returned(true);
    var big = returned(true);

    small.retainForDamage(new BigDecimal("300"), RETURN_DAY.plusDays(30));
    big.retainForDamage(new BigDecimal("3000"), RETURN_DAY.plusDays(2));

    assertThat(small.retained()).isEqualByComparingTo("300");
    assertThat(small.released()).isEqualByComparingTo("500");
    assertThat(small.status()).isEqualTo(DepositStatus.RELEASED);
    assertThat(big.retained()).isEqualByComparingTo("800");
    assertThat(big.released()).isEqualByComparingTo("0");
  }

  @Test
  void shouldNotRetainQuoteAfter30Days() {
    var deposit = returned(true);

    assertThatThrownBy(() -> deposit.retainForDamage(new BigDecimal("300"), RETURN_DAY.plusDays(31))).isExactlyInstanceOf(
      DamageQuoteTooLateException.class
    );
  }

  @Test
  void shouldNotRetainQuoteWithoutDeclaredDamage() {
    assertThatThrownBy(() -> returned(false).retainForDamage(new BigDecimal("300"), RETURN_DAY)).isExactlyInstanceOf(NoDamageDeclaredException.class);
    assertThatThrownBy(() -> held().retainForDamage(new BigDecimal("300"), RETURN_DAY)).isExactlyInstanceOf(NoDamageDeclaredException.class);
  }

  @Test
  void shouldNotRetainOnReleasedDeposit() {
    var deposit = returned(true);
    deposit.retainForDamage(new BigDecimal("300"), RETURN_DAY);

    assertThatThrownBy(() -> deposit.retainForDamage(new BigDecimal("300"), RETURN_DAY)).isExactlyInstanceOf(DepositAlreadyReleasedException.class);
  }
}

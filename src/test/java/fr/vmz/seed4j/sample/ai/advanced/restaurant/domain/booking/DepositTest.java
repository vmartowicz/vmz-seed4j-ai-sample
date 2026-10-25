package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking;

import static fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking.TableBookingsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.table.PartySize;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

@UnitTest
class DepositTest {

  @Test
  void shouldExpect20EurosPerPersonWithin48Hours() {
    var deposit = Deposit.expected(new PartySize(6), NOW, DINNER_TIME);

    assertThat(deposit.amount()).isEqualByComparingTo("120");
    assertThat(deposit.dueBy()).isEqualTo(NOW.plusHours(48));
    assertThat(deposit.status()).isEqualTo(DepositStatus.EXPECTED);
    assertThat(deposit.isPaid()).isFalse();
  }

  @Test
  void shouldExpectDepositBeforeMealWhenMealIsWithin48Hours() {
    var deposit = Deposit.expected(new PartySize(7), DINNER_TIME.minusHours(20), DINNER_TIME);

    assertThat(deposit.amount()).isEqualByComparingTo("140");
    assertThat(deposit.dueBy()).isEqualTo(DINNER_TIME);
  }

  @Test
  void shouldAcceptOnlyExactAmount() {
    var deposit = Deposit.expected(new PartySize(6), NOW, DINNER_TIME);

    assertThat(deposit.accepts(new BigDecimal("120.00"))).isTrue();
    assertThat(deposit.accepts(new BigDecimal("100"))).isFalse();
  }

  @Test
  void shouldFollowDepositStatuses() {
    var paid = Deposit.expected(new PartySize(6), NOW, DINNER_TIME).paid();

    assertThat(paid.isPaid()).isTrue();
    assertThat(paid.refunded().status()).isEqualTo(DepositStatus.REFUNDED);
    assertThat(paid.forfeited().status()).isEqualTo(DepositStatus.FORFEITED);
    assertThat(paid.forfeited().amount()).isEqualByComparingTo("120");
  }
}

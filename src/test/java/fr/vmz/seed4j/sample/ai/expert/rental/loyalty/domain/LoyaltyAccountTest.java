package fr.vmz.seed4j.sample.ai.expert.rental.loyalty.domain;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;

@UnitTest
class LoyaltyAccountTest {

  private static final LocalDate TODAY = LocalDate.of(2026, 6, 1);

  private final LoyaltyAccount account = LoyaltyAccount.open(new Email("jane@mail.com"));

  @Test
  void shouldStartAsMemberWithoutPoints() {
    assertThat(account.customer()).isEqualTo(new Email("jane@mail.com"));
    assertThat(account.balanceOn(TODAY)).isZero();
    assertThat(account.statusOn(TODAY)).isEqualTo(LoyaltyStatus.MEMBER);
  }

  @Test
  void shouldBecomeSilverWith1000PointsIn12Months() {
    account.credit(999, TODAY, "contract-1");
    assertThat(account.statusOn(TODAY)).isEqualTo(LoyaltyStatus.MEMBER);

    account.credit(1, TODAY, "contract-2");

    assertThat(account.statusOn(TODAY)).isEqualTo(LoyaltyStatus.SILVER);
    assertThat(account.earnedInLast12MonthsOn(TODAY)).isEqualTo(1000);
  }

  @Test
  void shouldBecomeGoldWith3000PointsIn12Months() {
    account.credit(3000, TODAY.minusMonths(12).plusDays(1), "contract-1");

    assertThat(account.statusOn(TODAY)).isEqualTo(LoyaltyStatus.GOLD);
    assertThat(account.statusOn(TODAY.plusDays(1))).isEqualTo(LoyaltyStatus.MEMBER);
  }

  @Test
  void shouldExpirePointsAfter24Months() {
    account.credit(800, TODAY, "contract-1");

    assertThat(account.balanceOn(TODAY.plusMonths(24).minusDays(1))).isEqualTo(800);
    assertThat(account.balanceOn(TODAY.plusMonths(24))).isZero();
  }

  @Test
  void shouldRedeem500PointsForAFreeDay() {
    account.credit(1200, TODAY, "contract-1");
    var reservation = UUID.randomUUID();

    account.redeemFreeDay(reservation, TODAY);

    assertThat(account.balanceOn(TODAY)).isEqualTo(700);
    assertThat(account.statusOn(TODAY)).isEqualTo(LoyaltyStatus.SILVER);
  }

  @Test
  void shouldNotRedeemWithoutEnoughPoints() {
    account.credit(499, TODAY, "contract-1");

    assertThatThrownBy(() -> account.redeemFreeDay(UUID.randomUUID(), TODAY)).isExactlyInstanceOf(NotEnoughPointsException.class).hasMessageContaining(
      "499"
    );
  }

  @Test
  void shouldGiveBackPointsOfCancelledReservation() {
    account.credit(600, TODAY, "contract-1");
    var reservation = UUID.randomUUID();
    account.redeemFreeDay(reservation, TODAY);

    account.cancelRedemption(UUID.randomUUID());
    assertThat(account.balanceOn(TODAY)).isEqualTo(100);
    account.cancelRedemption(reservation);

    assertThat(account.balanceOn(TODAY)).isEqualTo(600);
  }

  @Test
  void shouldNeverHaveNegativeBalance() {
    account.credit(500, TODAY, "contract-1");
    account.redeemFreeDay(UUID.randomUUID(), TODAY);

    assertThat(account.balanceOn(TODAY.plusYears(3))).isZero();
  }

  @Test
  void shouldNotCreditNegativePoints() {
    assertThatThrownBy(() -> account.credit(-1, TODAY, "contract-1")).isInstanceOf(AssertionException.class);
  }
}

package fr.vmz.seed4j.sample.ai.expert.gym.membership.domain;

import static fr.vmz.seed4j.sample.ai.expert.gym.membership.domain.MembershipsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;
import java.time.LocalDate;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@UnitTest
class MembershipToSubscribeTest {

  @Nested
  class MonthlyFee {

    @ParameterizedTest
    @CsvSource(
      {
        "ESSENTIAL, false, true, 29.90",
        "ESSENTIAL, false, false, 39.90",
        "PREMIUM, false, true, 49.90",
        "PREMIUM, false, false, 59.90",
        "ESSENTIAL, true, true, 20.93",
        "ESSENTIAL, true, false, 30.93",
        "PREMIUM, true, true, 49.90",
      }
    )
    void shouldPriceMonthlyFee(Plan plan, boolean studentCard, boolean committed, String fee) {
      var membership = new MembershipToSubscribe("Jane Doe", LocalDate.of(2004, 1, 1), false, studentCard, plan, committed, Gym.ECULLY);

      assertThat(membership.monthlyFee()).isEqualByComparingTo(fee);
    }
  }

  @Nested
  class Age {

    @Test
    void shouldNotSubscribeUnder16() {
      var membership = born(TODAY.minusYears(16).plusDays(1), true);

      assertThatThrownBy(() -> membership.subscribe(TODAY)).isExactlyInstanceOf(MemberTooYoungException.class).hasMessageContaining("16");
    }

    @Test
    void shouldSubscribeAt16WithParentalConsent() {
      assertThat(born(TODAY.minusYears(16), true).subscribe(TODAY).status()).isEqualTo(MembershipStatus.PENDING_PAYMENT);
    }

    @Test
    void shouldNotSubscribeUnder18WithoutParentalConsent() {
      var membership = born(TODAY.minusYears(18).plusDays(1), false);

      assertThatThrownBy(() -> membership.subscribe(TODAY)).isExactlyInstanceOf(ParentalConsentRequiredException.class);
    }

    @Test
    void shouldSubscribeAt18WithoutParentalConsent() {
      assertThatCode(() -> born(TODAY.minusYears(18), false).subscribe(TODAY)).doesNotThrowAnyException();
    }
  }

  @Test
  void shouldNotBuildWithoutName() {
    assertThatThrownBy(() -> new MembershipToSubscribe(" ", LocalDate.of(1990, 1, 1), false, false, Plan.PREMIUM, true, Gym.ECULLY)).isInstanceOf(
      AssertionException.class
    );
  }

  @Test
  void shouldNotBuildWithoutPlan() {
    assertThatThrownBy(() -> new MembershipToSubscribe("Jane Doe", LocalDate.of(1990, 1, 1), false, false, null, true, Gym.ECULLY)).isInstanceOf(
      AssertionException.class
    );
  }
}

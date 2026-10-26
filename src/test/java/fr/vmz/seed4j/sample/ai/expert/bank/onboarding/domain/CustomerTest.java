package fr.vmz.seed4j.sample.ai.expert.bank.onboarding.domain;

import static fr.vmz.seed4j.sample.ai.expert.bank.onboarding.domain.OnboardingFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class CustomerTest {

  @Test
  void shouldOnboardAdultWithCompleteFile() {
    var customer = customer();

    assertThat(customer.id()).isNotNull();
    assertThat(customer.fullName()).isEqualTo("Jane Doe");
    assertThat(customer.birthDate()).isEqualTo(LocalDate.of(1990, 5, 4));
    assertThat(customer.monthlyIncome()).isEqualByComparingTo("2500");
    assertThat(customer.risk()).isEqualTo(RiskLevel.LOW);
    assertThat(customer.customerSince()).isEqualTo(TODAY);
    assertThat(customer.isMinorOn(TODAY)).isFalse();
    assertThat(customer.legalGuardian()).isEmpty();
  }

  @Nested
  class Age {

    @Test
    void shouldOnboardMinorWithLegalGuardian() {
      var minor = born(TODAY.minusYears(12), "John Doe").onboard(TODAY);

      assertThat(minor.isMinorOn(TODAY)).isTrue();
      assertThat(minor.legalGuardian()).contains("John Doe");
      assertThat(minor.isMinorOn(TODAY.plusYears(6))).isFalse();
    }

    @Test
    void shouldNotOnboardMinorWithoutLegalGuardian() {
      assertThatThrownBy(() -> born(TODAY.minusYears(17), " ").onboard(TODAY)).isExactlyInstanceOf(IncompleteFileException.class).hasMessageContaining(
        "legal guardian"
      );
    }

    @Test
    void shouldNotOnboardChildrenUnder12() {
      assertThatThrownBy(() -> born(TODAY.minusYears(12).plusDays(1), "John Doe").onboard(TODAY))
        .isExactlyInstanceOf(IncompleteFileException.class)
        .hasMessageContaining("12");
    }
  }

  @Nested
  class Documents {

    @Test
    void shouldNotOnboardWithExpiredIdentityDocument() {
      assertThatThrownBy(() -> withDocuments(TODAY.minusDays(1), TODAY, true).onboard(TODAY)).isExactlyInstanceOf(IncompleteFileException.class).hasMessageContaining(
        "identity"
      );
    }

    @Test
    void shouldAcceptProofOfAddressOfLessThan3Months() {
      assertThatCode(() -> withDocuments(TODAY, TODAY.minusMonths(3), true).onboard(TODAY)).doesNotThrowAnyException();
    }

    @Test
    void shouldNotOnboardWithOldProofOfAddress() {
      assertThatThrownBy(() -> withDocuments(TODAY, TODAY.minusMonths(3).minusDays(1), true).onboard(TODAY)).isExactlyInstanceOf(
        IncompleteFileException.class
      ).hasMessageContaining("address");
    }

    @Test
    void shouldNotOnboardWithoutIncomeProof() {
      assertThatThrownBy(() -> withDocuments(TODAY, TODAY, false).onboard(TODAY)).isExactlyInstanceOf(IncompleteFileException.class).hasMessageContaining(
        "income"
      );
    }

    @Test
    void shouldNotOnboardWithoutName() {
      assertThatThrownBy(() ->
        new ProspectToOnboard(" ", LocalDate.of(1990, 1, 1), BigDecimal.ONE, TODAY, TODAY, true, false, RiskLevel.LOW, null)
      ).isInstanceOf(AssertionException.class);
    }
  }

  @Nested
  class Review {

    @Test
    void shouldReviewLowAndMediumRiskEvery3Years() {
      var customer = customer();

      assertThat(customer.nextReviewOn()).isEqualTo(TODAY.plusYears(3));
      assertThat(customer.isReviewOverdueOn(TODAY.plusYears(3))).isFalse();
      assertThat(customer.isReviewOverdueOn(TODAY.plusYears(3).plusDays(1))).isTrue();
    }

    @Test
    void shouldReviewHighRiskEveryYear() {
      var customer = new ProspectToOnboard("Jane Doe", LocalDate.of(1990, 5, 4), BigDecimal.TEN, TODAY.plusYears(1), TODAY, true, false, RiskLevel.HIGH, null).onboard(TODAY);

      assertThat(customer.nextReviewOn()).isEqualTo(TODAY.plusYears(1));
    }

    @Test
    void shouldAlwaysRatePoliticallyExposedPeopleAsHighRisk() {
      var politician = new ProspectToOnboard("Jane Doe", LocalDate.of(1990, 5, 4), BigDecimal.TEN, TODAY.plusYears(1), TODAY, true, true, RiskLevel.LOW, null).onboard(TODAY);

      politician.review(RiskLevel.MEDIUM, TODAY.plusMonths(6));

      assertThat(politician.risk()).isEqualTo(RiskLevel.HIGH);
      assertThat(politician.nextReviewOn()).isEqualTo(TODAY.plusMonths(18));
    }

    @Test
    void shouldStartNewCycleOnReview() {
      var customer = customer();

      customer.review(RiskLevel.HIGH, TODAY.plusYears(4));

      assertThat(customer.risk()).isEqualTo(RiskLevel.HIGH);
      assertThat(customer.isReviewOverdueOn(TODAY.plusYears(4).plusMonths(6))).isFalse();
    }
  }
}

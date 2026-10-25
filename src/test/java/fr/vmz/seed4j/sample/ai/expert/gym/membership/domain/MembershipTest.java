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
import org.junit.jupiter.params.provider.ValueSource;

@UnitTest
class MembershipTest {

  @Test
  void shouldWaitForFirstPaymentOnceSubscribed() {
    var membership = essential().subscribe(TODAY);

    assertThat(membership.id()).isNotNull();
    assertThat(membership.fullName()).isEqualTo("Jane Doe");
    assertThat(membership.plan()).isEqualTo(Plan.ESSENTIAL);
    assertThat(membership.homeGym()).isEqualTo(Gym.CONFLUENCE);
    assertThat(membership.committed()).isTrue();
    assertThat(membership.monthlyFee()).isEqualByComparingTo("29.90");
    assertThat(membership.subscribedOn()).isEqualTo(TODAY);
    assertThat(membership.status()).isEqualTo(MembershipStatus.PENDING_PAYMENT);
  }

  @Nested
  class SignUpFee {

    @Test
    void shouldCharge39EurosOfSignUpFee() {
      assertThat(essential().subscribe(TODAY).signUpFee()).isEqualByComparingTo("39");
    }

    @ParameterizedTest
    @ValueSource(strings = { "2026-01-31", "2026-09-01" })
    void shouldOfferSignUpFeeInJanuaryAndSeptember(String date) {
      assertThat(essential().subscribe(LocalDate.parse(date)).signUpFee()).isEqualByComparingTo("0");
    }
  }

  @Nested
  class Lifecycle {

    @Test
    void shouldActivateOnFirstPayment() {
      assertThat(activeMembership().statusOn(TODAY)).isEqualTo(MembershipStatus.ACTIVE);
    }

    @Test
    void shouldNotActivateTwice() {
      var membership = activeMembership();

      assertThatThrownBy(membership::activate).isExactlyInstanceOf(InvalidMembershipTransitionException.class).hasMessageContaining("ACTIVE");
    }

    @Test
    void shouldTerminate() {
      var membership = activeMembership();

      membership.terminate();

      assertThat(membership.statusOn(TODAY)).isEqualTo(MembershipStatus.TERMINATED);
    }

    @Test
    void shouldNotTerminateTwice() {
      var membership = activeMembership();
      membership.terminate();

      assertThatThrownBy(membership::terminate).isExactlyInstanceOf(InvalidMembershipTransitionException.class);
    }
  }

  @Nested
  class Gyms {

    @Test
    void shouldGiveEssentialMembersTheirHomeGym() {
      assertThat(activeMembership().accessibleGyms()).containsExactly(Gym.CONFLUENCE);
    }

    @Test
    void shouldGivePremiumMembersAllGyms() {
      assertThat(premiumWithoutCommitment().subscribe(TODAY).accessibleGyms()).containsExactlyInAnyOrder(Gym.values());
    }
  }

  @Nested
  class Suspend {

    @Test
    void shouldSuspendFromStartUntilEnd() {
      var membership = activeMembership();

      membership.suspend(new Suspension(TODAY.plusDays(5), 2), TODAY);

      assertThat(membership.statusOn(TODAY.plusDays(4))).isEqualTo(MembershipStatus.ACTIVE);
      assertThat(membership.statusOn(TODAY.plusDays(5))).isEqualTo(MembershipStatus.SUSPENDED);
      assertThat(membership.isSuspendedOn(TODAY.plusDays(5).plusMonths(2).minusDays(1))).isTrue();
      assertThat(membership.isSuspendedOn(TODAY.plusDays(5).plusMonths(2))).isFalse();
    }

    @Test
    void shouldExtendCommitmentBySuspension() {
      var membership = activeMembership();
      assertThat(membership.commitmentEnd()).contains(TODAY.plusMonths(12));

      membership.suspend(new Suspension(TODAY, 3), TODAY);

      assertThat(membership.commitmentEnd()).contains(TODAY.plusMonths(15));
    }

    @Test
    void shouldHaveNoCommitmentEndWithoutCommitment() {
      assertThat(premiumWithoutCommitment().subscribe(TODAY).commitmentEnd()).isEmpty();
    }

    @Test
    void shouldNotSuspendPendingMembership() {
      var membership = essential().subscribe(TODAY);

      assertThatThrownBy(() -> membership.suspend(new Suspension(TODAY, 1), TODAY)).isExactlyInstanceOf(
        InvalidMembershipTransitionException.class
      );
    }

    @Test
    void shouldNotSuspendInThePast() {
      var membership = activeMembership();

      assertThatThrownBy(() -> membership.suspend(new Suspension(TODAY.minusDays(1), 1), TODAY)).isExactlyInstanceOf(
        SuspensionInThePastException.class
      );
    }

    @Test
    void shouldSuspendOnlyOncePerYear() {
      var membership = activeMembership();
      membership.suspend(new Suspension(TODAY, 1), TODAY);

      assertThatThrownBy(() -> membership.suspend(new Suspension(LocalDate.of(2026, 12, 1), 1), TODAY))
        .isExactlyInstanceOf(SuspensionAlreadyUsedException.class)
        .hasMessageContaining("2026");
    }

    @Test
    void shouldSuspendAgainTheNextYear() {
      var membership = activeMembership();
      membership.suspend(new Suspension(TODAY, 1), TODAY);

      membership.suspend(new Suspension(LocalDate.of(2027, 1, 4), 1), TODAY.plusMonths(2));

      assertThat(membership.isSuspendedOn(LocalDate.of(2027, 1, 4))).isTrue();
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 4 })
    void shouldSuspendFrom1To3Months(int months) {
      assertThatThrownBy(() -> new Suspension(TODAY, months)).isInstanceOf(AssertionException.class);
    }
  }
}

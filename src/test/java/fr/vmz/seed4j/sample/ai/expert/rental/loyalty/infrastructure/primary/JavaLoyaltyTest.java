package fr.vmz.seed4j.sample.ai.expert.rental.loyalty.infrastructure.primary;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.rental.loyalty.application.LoyaltyApplicationService;
import fr.vmz.seed4j.sample.ai.expert.rental.loyalty.domain.LoyaltyStatus;
import fr.vmz.seed4j.sample.ai.expert.rental.loyalty.domain.NotEnoughPointsException;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import java.util.UUID;
import org.junit.jupiter.api.Test;

@UnitTest
class JavaLoyaltyTest {

  private static final Email JANE = new Email("jane@mail.com");

  private final LoyaltyApplicationService loyalty = mock(LoyaltyApplicationService.class);
  private final JavaLoyalty javaLoyalty = new JavaLoyalty(loyalty);

  @Test
  void shouldKnowGoldMembers() {
    doReturn(LoyaltyStatus.GOLD).when(loyalty).statusOf(JANE);

    assertThat(javaLoyalty.isGold("jane@mail.com")).isTrue();
    assertThat(javaLoyalty.isGold("john@mail.com")).isFalse();
  }

  @Test
  void shouldRedeemFreeDay() {
    var reservation = UUID.randomUUID();

    assertThat(javaLoyalty.redeemFreeDay("jane@mail.com", reservation)).isTrue();

    verify(loyalty).redeemFreeDay(JANE, reservation);
  }

  @Test
  void shouldTellWhenPointsAreMissing() {
    var reservation = UUID.randomUUID();
    doThrow(new NotEnoughPointsException(100)).when(loyalty).redeemFreeDay(JANE, reservation);

    assertThat(javaLoyalty.redeemFreeDay("jane@mail.com", reservation)).isFalse();
  }

  @Test
  void shouldCancelRedemptionAndCreditPoints() {
    var reservation = UUID.randomUUID();

    javaLoyalty.cancelRedemption("jane@mail.com", reservation);
    javaLoyalty.credit("jane@mail.com", 120, "contract-1");

    verify(loyalty).cancelRedemption(JANE, reservation);
    verify(loyalty).credit(JANE, 120, "contract-1");
  }
}

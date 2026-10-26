package fr.vmz.seed4j.sample.ai.expert.rental.reservation.infrastructure.secondary.loyalty;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.rental.loyalty.infrastructure.primary.JavaLoyalty;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import java.util.UUID;
import org.junit.jupiter.api.Test;

@UnitTest
class ReservationLoyaltyCardTest {

  private final JavaLoyalty loyalty = mock(JavaLoyalty.class);
  private final ReservationLoyaltyCard card = new ReservationLoyaltyCard(loyalty);

  @Test
  void shouldAskLoyaltyProgram() {
    var reservation = UUID.randomUUID();
    doReturn(true).when(loyalty).isGold("jane@mail.com");
    doReturn(true).when(loyalty).redeemFreeDay("jane@mail.com", reservation);

    assertThat(card.isGold(new Email("jane@mail.com"))).isTrue();
    assertThat(card.redeemFreeDay(new Email("jane@mail.com"), reservation)).isTrue();
    card.cancelRedemption(new Email("jane@mail.com"), reservation);

    verify(loyalty).cancelRedemption("jane@mail.com", reservation);
  }
}

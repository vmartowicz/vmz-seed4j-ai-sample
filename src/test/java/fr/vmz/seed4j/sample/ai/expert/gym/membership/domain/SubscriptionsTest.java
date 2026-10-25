package fr.vmz.seed4j.sample.ai.expert.gym.membership.domain;

import static fr.vmz.seed4j.sample.ai.expert.gym.membership.domain.MembershipsFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;

@UnitTest
class SubscriptionsTest {

  private final MembershipsRepository memberships = mock(MembershipsRepository.class);
  private final MembershipBilling billing = mock(MembershipBilling.class);
  private final MemberBadges badges = mock(MemberBadges.class);
  private final Subscriptions subscriptions = new Subscriptions(memberships, billing, badges);

  @Test
  void shouldOpenBillingAccountAndIssueBadge() {
    var invoice = new FirstInvoice(UUID.randomUUID(), new BigDecimal("60.43"));
    doReturn(invoice).when(billing).openAccount(any());
    doReturn("FC-12345678").when(badges).issueFor(any());

    var subscription = subscriptions.subscribe(essential(), TODAY);

    assertThat(subscription.firstInvoice()).isEqualTo(invoice);
    assertThat(subscription.badge()).isEqualTo("FC-12345678");
    verify(memberships).save(subscription.membership());
    verify(billing).openAccount(subscription.membership());
    verify(badges).issueFor(subscription.membership().id());
  }

  @Test
  void shouldNotOpenAccountForRefusedSubscription() {
    assertThatThrownBy(() -> subscriptions.subscribe(born(TODAY.minusYears(15), true), TODAY)).isExactlyInstanceOf(MemberTooYoungException.class);
    verifyNoInteractions(memberships, billing, badges);
  }
}

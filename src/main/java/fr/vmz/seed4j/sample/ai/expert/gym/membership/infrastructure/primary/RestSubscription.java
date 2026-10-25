package fr.vmz.seed4j.sample.ai.expert.gym.membership.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.gym.membership.domain.FirstInvoice;
import fr.vmz.seed4j.sample.ai.expert.gym.membership.domain.Subscription;
import java.time.LocalDate;

record RestSubscription(RestMembership membership, String badge, FirstInvoice firstInvoice) {
  static RestSubscription from(Subscription subscription, LocalDate today) {
    return new RestSubscription(RestMembership.from(subscription.membership(), today), subscription.badge(), subscription.firstInvoice());
  }
}

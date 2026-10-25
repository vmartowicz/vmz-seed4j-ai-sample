package fr.vmz.seed4j.sample.ai.expert.gym.membership.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDate;

public class Subscriptions {

  private final MembershipsRepository memberships;
  private final MembershipBilling billing;
  private final MemberBadges badges;

  public Subscriptions(MembershipsRepository memberships, MembershipBilling billing, MemberBadges badges) {
    this.memberships = memberships;
    this.billing = billing;
    this.badges = badges;
  }

  public Subscription subscribe(MembershipToSubscribe subscription, LocalDate today) {
    Assert.notNull("subscription", subscription);

    var membership = subscription.subscribe(today);
    memberships.save(membership);

    return new Subscription(membership, billing.openAccount(membership), badges.issueFor(membership.id()));
  }
}

package fr.vmz.seed4j.sample.ai.expert.gym.membership.application;

import fr.vmz.seed4j.sample.ai.expert.gym.membership.domain.MemberBadges;
import fr.vmz.seed4j.sample.ai.expert.gym.membership.domain.Membership;
import fr.vmz.seed4j.sample.ai.expert.gym.membership.domain.MembershipBilling;
import fr.vmz.seed4j.sample.ai.expert.gym.membership.domain.MembershipToSubscribe;
import fr.vmz.seed4j.sample.ai.expert.gym.membership.domain.MembershipsRepository;
import fr.vmz.seed4j.sample.ai.expert.gym.membership.domain.Subscription;
import fr.vmz.seed4j.sample.ai.expert.gym.membership.domain.Subscriptions;
import fr.vmz.seed4j.sample.ai.expert.gym.membership.domain.Suspension;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MembershipsApplicationService {

  private final MembershipsRepository memberships;
  private final Subscriptions subscriptions;
  private final Clock clock;

  public MembershipsApplicationService(MembershipsRepository memberships, MembershipBilling billing, MemberBadges badges, Clock clock) {
    this.memberships = memberships;
    subscriptions = new Subscriptions(memberships, billing, badges);
    this.clock = clock;
  }

  public Subscription subscribe(MembershipToSubscribe subscription) {
    return subscriptions.subscribe(subscription, today());
  }

  public Membership membership(MemberId member) {
    return memberships.membership(member);
  }

  public Optional<Membership> find(MemberId member) {
    return memberships.get(member);
  }

  public Membership suspend(MemberId member, Suspension suspension) {
    var membership = memberships.membership(member);

    membership.suspend(suspension, today());
    memberships.save(membership);

    return membership;
  }

  public void activate(MemberId member) {
    var membership = memberships.membership(member);

    membership.activate();
    memberships.save(membership);
  }

  public void terminate(MemberId member) {
    var membership = memberships.membership(member);

    membership.terminate();
    memberships.save(membership);
  }

  public LocalDate today() {
    return LocalDate.now(clock);
  }
}

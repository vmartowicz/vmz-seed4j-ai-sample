package fr.vmz.seed4j.sample.ai.expert.gym.membership.infrastructure.secondary.access;

import fr.vmz.seed4j.sample.ai.expert.gym.access.infrastructure.primary.JavaBadges;
import fr.vmz.seed4j.sample.ai.expert.gym.membership.domain.MemberBadges;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
class MembershipBadgesAdapter implements MemberBadges {

  private final JavaBadges badges;

  // Lazy: access reads memberships back, the contexts depend on each other
  MembershipBadgesAdapter(@Lazy JavaBadges badges) {
    this.badges = badges;
  }

  @Override
  public String issueFor(MemberId member) {
    return badges.issueFor(member.get());
  }
}

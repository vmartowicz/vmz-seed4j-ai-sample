package fr.vmz.seed4j.sample.ai.expert.gym.membership.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.gym.membership.application.MembershipsApplicationService;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class JavaMemberships {

  private final MembershipsApplicationService memberships;

  public JavaMemberships(MembershipsApplicationService memberships) {
    this.memberships = memberships;
  }

  public void activate(UUID member) {
    memberships.activate(new MemberId(member));
  }

  public void terminate(UUID member) {
    memberships.terminate(new MemberId(member));
  }

  public boolean isSuspendedOn(UUID member, LocalDate date) {
    return memberships.membership(new MemberId(member)).isSuspendedOn(date);
  }

  public Optional<JavaMembership> membership(UUID member) {
    var today = memberships.today();

    return memberships.find(new MemberId(member)).map(membership -> JavaMembership.from(membership, today));
  }
}

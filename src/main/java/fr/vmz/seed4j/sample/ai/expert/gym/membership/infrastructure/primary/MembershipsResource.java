package fr.vmz.seed4j.sample.ai.expert.gym.membership.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.gym.membership.application.MembershipsApplicationService;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gym/memberships")
class MembershipsResource {

  private final MembershipsApplicationService memberships;

  MembershipsResource(MembershipsApplicationService memberships) {
    this.memberships = memberships;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  RestSubscription subscribe(@RequestBody RestMembershipToSubscribe subscription) {
    return RestSubscription.from(memberships.subscribe(subscription.toDomain()), memberships.today());
  }

  @GetMapping("/{memberId}")
  RestMembership membership(@PathVariable UUID memberId) {
    return RestMembership.from(memberships.membership(new MemberId(memberId)), memberships.today());
  }

  @PostMapping("/{memberId}/suspension")
  RestMembership suspend(@PathVariable UUID memberId, @RequestBody RestSuspension suspension) {
    return RestMembership.from(memberships.suspend(new MemberId(memberId), suspension.toDomain()), memberships.today());
  }
}

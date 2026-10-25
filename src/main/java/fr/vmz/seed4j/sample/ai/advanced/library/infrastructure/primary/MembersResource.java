package fr.vmz.seed4j.sample.ai.advanced.library.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.library.application.MembersApplicationService;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.MemberId;
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
@RequestMapping("/api/library/members")
class MembersResource {

  private final MembersApplicationService members;

  MembersResource(MembersApplicationService members) {
    this.members = members;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  RestMember register(@RequestBody RestMemberToRegister member) {
    return RestMember.from(members.register(member.toDomain()));
  }

  @GetMapping("/{memberId}")
  RestMember get(@PathVariable UUID memberId) {
    return RestMember.from(members.get(new MemberId(memberId)));
  }
}

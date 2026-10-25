package fr.vmz.seed4j.sample.ai.advanced.library.application;

import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.Member;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.MemberId;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.MemberToRegister;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.MembersRegistrar;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.MembersRepository;
import java.time.Clock;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class MembersApplicationService {

  private final MembersRepository members;
  private final MembersRegistrar registrar;
  private final Clock clock;

  public MembersApplicationService(MembersRepository members, Clock clock) {
    this.members = members;
    registrar = new MembersRegistrar(members);
    this.clock = clock;
  }

  public Member register(MemberToRegister member) {
    return registrar.register(member, LocalDate.now(clock));
  }

  public Member get(MemberId member) {
    return members.member(member);
  }
}

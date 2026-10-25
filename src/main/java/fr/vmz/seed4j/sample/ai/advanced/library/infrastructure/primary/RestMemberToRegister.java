package fr.vmz.seed4j.sample.ai.advanced.library.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.FullName;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.MemberEmail;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.MemberToRegister;
import org.jspecify.annotations.Nullable;

record RestMemberToRegister(@Nullable String fullName, @Nullable String email) {
  MemberToRegister toDomain() {
    return new MemberToRegister(new FullName(fullName), new MemberEmail(email));
  }
}

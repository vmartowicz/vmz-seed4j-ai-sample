package fr.vmz.seed4j.sample.ai.advanced.library.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.Member;
import java.time.LocalDate;
import java.util.UUID;

record RestMember(UUID id, String fullName, String email, LocalDate registeredOn, String status) {
  static RestMember from(Member member) {
    return new RestMember(member.id().get(), member.name().get(), member.email().get(), member.registeredOn(), member.status().name());
  }
}

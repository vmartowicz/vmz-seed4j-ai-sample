package fr.vmz.seed4j.sample.ai.advanced.library.domain.member;

import fr.vmz.seed4j.sample.ai.advanced.library.domain.LibraryRuleViolationException;

class DuplicatedMemberEmailException extends LibraryRuleViolationException {

  DuplicatedMemberEmailException(MemberEmail email) {
    super("A member is already registered with " + email.get());
  }
}

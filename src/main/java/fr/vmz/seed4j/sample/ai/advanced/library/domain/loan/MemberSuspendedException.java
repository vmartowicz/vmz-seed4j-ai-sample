package fr.vmz.seed4j.sample.ai.advanced.library.domain.loan;

import fr.vmz.seed4j.sample.ai.advanced.library.domain.LibraryRuleViolationException;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.MemberId;

class MemberSuspendedException extends LibraryRuleViolationException {

  MemberSuspendedException(MemberId member) {
    super("Member " + member.get() + " is suspended");
  }
}

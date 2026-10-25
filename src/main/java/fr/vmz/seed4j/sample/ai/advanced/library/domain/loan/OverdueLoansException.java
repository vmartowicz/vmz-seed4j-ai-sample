package fr.vmz.seed4j.sample.ai.advanced.library.domain.loan;

import fr.vmz.seed4j.sample.ai.advanced.library.domain.LibraryRuleViolationException;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.MemberId;

class OverdueLoansException extends LibraryRuleViolationException {

  OverdueLoansException(MemberId member) {
    super("Member " + member.get() + " must first return overdue books");
  }
}

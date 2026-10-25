package fr.vmz.seed4j.sample.ai.advanced.library.domain.loan;

import fr.vmz.seed4j.sample.ai.advanced.library.domain.LibraryRuleViolationException;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.MemberId;

class BorrowingLimitReachedException extends LibraryRuleViolationException {

  BorrowingLimitReachedException(MemberId member) {
    super("Member " + member.get() + " already borrows 3 books");
  }
}

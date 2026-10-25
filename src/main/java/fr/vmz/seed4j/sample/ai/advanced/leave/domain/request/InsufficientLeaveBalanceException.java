package fr.vmz.seed4j.sample.ai.advanced.leave.domain.request;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.LeaveDays;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.LeaveRuleViolationException;

class InsufficientLeaveBalanceException extends LeaveRuleViolationException {

  InsufficientLeaveBalanceException(LeaveDays remaining, LeaveDays requested) {
    super("Only " + remaining.get() + " days remain, " + requested.get() + " requested");
  }
}

package fr.vmz.seed4j.sample.ai.advanced.leave.domain.request;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.LeaveRuleViolationException;

class LeaveInThePastException extends LeaveRuleViolationException {

  LeaveInThePastException(LeavePeriod period) {
    super("A leave can't start in the past: " + period.start().date());
  }
}

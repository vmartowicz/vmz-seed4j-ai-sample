package fr.vmz.seed4j.sample.ai.advanced.leave.domain.request;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.LeaveRuleViolationException;

class OverlappingLeaveException extends LeaveRuleViolationException {

  OverlappingLeaveException(LeavePeriod period) {
    super("This leave overlaps another pending or approved leave starting on " + period.start().date());
  }
}

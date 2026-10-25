package fr.vmz.seed4j.sample.ai.advanced.leave.domain.request;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.LeaveRuleViolationException;

class LeaveRequestNotPendingException extends LeaveRuleViolationException {

  LeaveRequestNotPendingException(LeaveRequestId request, LeaveRequestStatus status) {
    super("Leave request " + request.get() + " is " + status + ", it can no longer change");
  }
}

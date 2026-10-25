package fr.vmz.seed4j.sample.ai.advanced.leave.domain.request;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.UnknownLeaveElementException;

class UnknownLeaveRequestException extends UnknownLeaveElementException {

  UnknownLeaveRequestException(LeaveRequestId request) {
    super("Unknown leave request " + request.get());
  }
}

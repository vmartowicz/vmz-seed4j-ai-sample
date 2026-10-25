package fr.vmz.seed4j.sample.ai.advanced.leave.domain.request;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.InvalidLeaveValueException;

class InvalidLeavePeriodException extends InvalidLeaveValueException {

  InvalidLeavePeriodException(String reason) {
    super(reason);
  }
}

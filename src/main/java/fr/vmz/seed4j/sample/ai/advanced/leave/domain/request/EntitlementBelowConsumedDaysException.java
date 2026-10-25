package fr.vmz.seed4j.sample.ai.advanced.leave.domain.request;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.LeaveDays;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.LeaveRuleViolationException;

class EntitlementBelowConsumedDaysException extends LeaveRuleViolationException {

  EntitlementBelowConsumedDaysException(LeaveDays consumed) {
    super("The entitlement can't be lower than the " + consumed.get() + " days already approved or pending");
  }
}

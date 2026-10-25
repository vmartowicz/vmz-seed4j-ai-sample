package fr.vmz.seed4j.sample.ai.advanced.leave.domain;

import java.math.BigDecimal;

class InvalidLeaveDaysException extends InvalidLeaveValueException {

  InvalidLeaveDaysException(BigDecimal days) {
    super("Leave is counted in half days, got: " + days.toPlainString());
  }
}

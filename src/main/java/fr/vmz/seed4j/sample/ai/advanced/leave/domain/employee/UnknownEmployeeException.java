package fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.UnknownLeaveElementException;

public class UnknownEmployeeException extends UnknownLeaveElementException {

  UnknownEmployeeException(EmployeeId employee) {
    super("Unknown employee " + employee.get());
  }
}

package fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking;

import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.MeetingRoomRuleViolationException;

class NotTheBookerException extends MeetingRoomRuleViolationException {

  NotTheBookerException(EmployeeEmail employee) {
    super("Only the employee who booked can cancel, not " + employee.get());
  }
}

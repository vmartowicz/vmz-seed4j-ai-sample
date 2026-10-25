package fr.vmz.seed4j.sample.ai.simple.meetingroom.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking.EmployeeEmail;
import org.jspecify.annotations.Nullable;

record RestEmployee(@Nullable String employeeEmail) {
  EmployeeEmail toDomain() {
    return new EmployeeEmail(employeeEmail);
  }
}

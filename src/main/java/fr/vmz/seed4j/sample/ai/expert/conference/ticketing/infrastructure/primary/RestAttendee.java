package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.AttendeeToRegister;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import org.jspecify.annotations.Nullable;

record RestAttendee(@Nullable String name, @Nullable String email, boolean student) {
  AttendeeToRegister toDomain() {
    return new AttendeeToRegister(name, new Email(email), student);
  }
}

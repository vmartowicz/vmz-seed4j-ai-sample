package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.RegistrationStatus;
import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.WorkshopRegistration;
import java.time.LocalDateTime;

record RestRegistration(String ticket, RegistrationStatus status, LocalDateTime requestedAt) {
  static RestRegistration from(WorkshopRegistration registration) {
    return new RestRegistration(registration.ticket().get(), registration.status(), registration.requestedAt());
  }
}

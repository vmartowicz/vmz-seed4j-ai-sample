package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;

public record AttendeeToRegister(String name, Email email, boolean student) {
  Holder holder() {
    return new Holder(name, email);
  }
}

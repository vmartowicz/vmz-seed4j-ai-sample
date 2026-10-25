package fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain;

import java.time.LocalDate;

class PaymentDeadlinePassedException extends SponsoringRuleViolationException {

  PaymentDeadlinePassedException(SponsorId sponsorship, LocalDate deadline) {
    super("The payment of sponsorship " + sponsorship.get() + " was due by " + deadline);
  }
}

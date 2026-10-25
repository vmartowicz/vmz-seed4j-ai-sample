package fr.vmz.seed4j.sample.ai.expert.gym.billing.domain;

import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;

class UnknownBillingAccountException extends UnknownBillingElementException {

  UnknownBillingAccountException(MemberId member) {
    super("No billing account for member " + member.get());
  }
}

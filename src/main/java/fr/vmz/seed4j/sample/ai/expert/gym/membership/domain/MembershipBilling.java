package fr.vmz.seed4j.sample.ai.expert.gym.membership.domain;

public interface MembershipBilling {
  FirstInvoice openAccount(Membership membership);
}

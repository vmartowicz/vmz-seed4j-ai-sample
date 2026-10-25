package fr.vmz.seed4j.sample.ai.expert.gym.billing.domain;

public interface Bank {
  void submitDebit(Invoice invoice);
}

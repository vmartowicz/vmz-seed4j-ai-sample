package fr.vmz.seed4j.sample.ai.expert.gym.billing.domain;

public enum InvoiceStatus {
  AWAITING_PAYMENT,
  DEBIT_SUBMITTED,
  DEBIT_RETRY_SCHEDULED,
  PAID,
  UNPAID,
}

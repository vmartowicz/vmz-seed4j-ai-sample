package fr.vmz.seed4j.sample.ai.expert.gym.billing.infrastructure.secondary.bank;

import fr.vmz.seed4j.sample.ai.expert.gym.billing.domain.Bank;
import fr.vmz.seed4j.sample.ai.expert.gym.billing.domain.Invoice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class LoggingBank implements Bank {

  private static final Logger log = LoggerFactory.getLogger(LoggingBank.class);

  @Override
  public void submitDebit(Invoice invoice) {
    log.info("Direct debit {} of {} submitted for invoice {}", invoice.attempts(), invoice.amount().get(), invoice.id().get());
  }
}

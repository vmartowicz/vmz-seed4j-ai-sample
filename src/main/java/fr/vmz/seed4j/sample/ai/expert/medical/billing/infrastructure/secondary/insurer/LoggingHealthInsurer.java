package fr.vmz.seed4j.sample.ai.expert.medical.billing.infrastructure.secondary.insurer;

import fr.vmz.seed4j.sample.ai.expert.medical.billing.domain.CareSheet;
import fr.vmz.seed4j.sample.ai.expert.medical.billing.domain.HealthInsurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class LoggingHealthInsurer implements HealthInsurer {

  private static final Logger log = LoggerFactory.getLogger(LoggingHealthInsurer.class);

  @Override
  public void send(CareSheet sheet) {
    log.info("Care sheet {} sent for insured {}: {} reimbursed", sheet.id().get(), sheet.insured().get(), sheet.reimbursement());
  }
}

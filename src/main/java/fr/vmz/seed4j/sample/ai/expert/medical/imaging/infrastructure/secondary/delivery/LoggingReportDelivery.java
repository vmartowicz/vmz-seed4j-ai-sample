package fr.vmz.seed4j.sample.ai.expert.medical.imaging.infrastructure.secondary.delivery;

import fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.Examination;
import fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.ReportDelivery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class LoggingReportDelivery implements ReportDelivery {

  private static final Logger log = LoggerFactory.getLogger(LoggingReportDelivery.class);

  @Override
  public void deliver(Examination examination) {
    log.info(
      "Report of {} {} sent to doctor {} and patient {}",
      examination.exam(),
      examination.id().get(),
      examination.prescriber().get(),
      examination.patient().get()
    );
  }
}

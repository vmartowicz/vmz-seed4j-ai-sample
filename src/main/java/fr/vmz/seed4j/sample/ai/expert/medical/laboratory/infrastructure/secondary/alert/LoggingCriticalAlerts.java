package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.infrastructure.secondary.alert;

import fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain.CriticalAlert;
import fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain.CriticalAlerts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class LoggingCriticalAlerts implements CriticalAlerts {

  private static final Logger log = LoggerFactory.getLogger(LoggingCriticalAlerts.class);

  @Override
  public void send(CriticalAlert alert) {
    log.warn(
      "Critical {} result {} for patient {} (sample {}): doctor {} alerted",
      alert.test(),
      alert.value(),
      alert.patient().get(),
      alert.sample().get(),
      alert.recipient().get()
    );
  }
}

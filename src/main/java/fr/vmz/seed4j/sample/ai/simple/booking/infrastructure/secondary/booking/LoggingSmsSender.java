package fr.vmz.seed4j.sample.ai.simple.booking.infrastructure.secondary.booking;

import fr.vmz.seed4j.sample.ai.simple.booking.domain.booking.Sms;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.booking.SmsSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class LoggingSmsSender implements SmsSender {

  private static final Logger log = LoggerFactory.getLogger(LoggingSmsSender.class);

  @Override
  public void send(Sms sms) {
    log.info("SMS to {}: {}", sms.to().get(), sms.text());
  }
}

package fr.vmz.seed4j.sample.ai.advanced.leave.infrastructure.secondary.request;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.LeaveDecision;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.LeaveNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class LoggingLeaveNotifier implements LeaveNotifier {

  private static final Logger log = LoggerFactory.getLogger(LoggingLeaveNotifier.class);

  @Override
  public void decisionMade(LeaveDecision decision) {
    var period = decision.request().period();

    log.info(
      "Email to {}: your leave from {} {} to {} {} is {}{}",
      decision.to().get(),
      period.start().date(),
      period.start().half(),
      period.end().date(),
      period.end().half(),
      decision.request().status(),
      decision.comment().map(comment -> " (" + comment.get() + ")").orElse("")
    );
  }
}

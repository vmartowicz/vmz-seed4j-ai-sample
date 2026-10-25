package fr.vmz.seed4j.sample.ai.advanced.leave.infrastructure.secondary.request;

import static fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeesFixture.*;
import static fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.LeaveRequestsFixture.*;

import ch.qos.logback.classic.Level;
import fr.vmz.seed4j.sample.ai.Logs;
import fr.vmz.seed4j.sample.ai.LogsSpy;
import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.LeaveDecision;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.RejectionComment;
import java.util.Optional;
import org.junit.jupiter.api.Test;

@UnitTest
class LoggingLeaveNotifierTest {

  @Logs
  private LogsSpy logs;

  private final LoggingLeaveNotifier notifier = new LoggingLeaveNotifier();

  @Test
  void shouldLogApprovalEmail() {
    notifier.decisionMade(new LeaveDecision(EMMA.email(), approved(EMMA, fullDay(MONDAY)), Optional.empty()));

    logs.shouldHave(Level.INFO, "Email to emma@company.com: your leave from 2026-04-06 MORNING to 2026-04-06 AFTERNOON is APPROVED");
  }

  @Test
  void shouldLogRejectionEmailWithComment() {
    var request = pending(EMMA, fullDay(MONDAY));
    var comment = Optional.of(new RejectionComment("Release week"));
    request.reject(comment);

    notifier.decisionMade(new LeaveDecision(EMMA.email(), request, comment));

    logs.shouldHave(Level.INFO, "is REJECTED (Release week)");
  }
}

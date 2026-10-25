package fr.vmz.seed4j.sample.ai.expert.gym.schedule.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.InvalidScheduleValueException;
import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.ScheduleRuleViolationException;
import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.UnknownScheduleElementException;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackageClasses = ScheduleErrorsHandler.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
class ScheduleErrorsHandler {

  @ExceptionHandler(UnknownScheduleElementException.class)
  ProblemDetail handleUnknownElement(UnknownScheduleElementException exception) {
    return problem(HttpStatus.NOT_FOUND, "Unknown element", exception);
  }

  @ExceptionHandler(ScheduleRuleViolationException.class)
  ProblemDetail handleRuleViolation(ScheduleRuleViolationException exception) {
    return problem(HttpStatus.CONFLICT, "Business rule violation", exception);
  }

  @ExceptionHandler(InvalidScheduleValueException.class)
  ProblemDetail handleInvalidValue(InvalidScheduleValueException exception) {
    return problem(HttpStatus.BAD_REQUEST, "Invalid value", exception);
  }

  @ExceptionHandler(AssertionException.class)
  ProblemDetail handleAssertion(AssertionException exception) {
    return problem(HttpStatus.BAD_REQUEST, "Invalid value", exception);
  }

  private ProblemDetail problem(HttpStatus status, String title, RuntimeException exception) {
    var problem = ProblemDetail.forStatusAndDetail(status, exception.getMessage());
    problem.setTitle(title);

    return problem;
  }
}

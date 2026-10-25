package fr.vmz.seed4j.sample.ai.advanced.leave.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.InvalidLeaveValueException;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.LeaveAccessDeniedException;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.LeaveRuleViolationException;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.UnknownLeaveElementException;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackageClasses = LeaveErrorsHandler.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
class LeaveErrorsHandler {

  @ExceptionHandler(UnknownLeaveElementException.class)
  ProblemDetail handleUnknownElement(UnknownLeaveElementException exception) {
    return problem(HttpStatus.NOT_FOUND, "Unknown element", exception);
  }

  @ExceptionHandler(LeaveAccessDeniedException.class)
  ProblemDetail handleAccessDenied(LeaveAccessDeniedException exception) {
    return problem(HttpStatus.FORBIDDEN, "Access denied", exception);
  }

  @ExceptionHandler(LeaveRuleViolationException.class)
  ProblemDetail handleRuleViolation(LeaveRuleViolationException exception) {
    return problem(HttpStatus.CONFLICT, "Business rule violation", exception);
  }

  @ExceptionHandler(InvalidLeaveValueException.class)
  ProblemDetail handleInvalidValue(InvalidLeaveValueException exception) {
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

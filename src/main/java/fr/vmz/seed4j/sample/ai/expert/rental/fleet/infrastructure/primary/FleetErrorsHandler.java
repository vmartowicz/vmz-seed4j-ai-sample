package fr.vmz.seed4j.sample.ai.expert.rental.fleet.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain.FleetRuleViolationException;
import fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain.InvalidFleetValueException;
import fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain.UnknownFleetElementException;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackageClasses = FleetErrorsHandler.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
class FleetErrorsHandler {

  @ExceptionHandler(UnknownFleetElementException.class)
  ProblemDetail handleUnknownElement(UnknownFleetElementException exception) {
    return problem(HttpStatus.NOT_FOUND, "Unknown element", exception);
  }

  @ExceptionHandler(FleetRuleViolationException.class)
  ProblemDetail handleRuleViolation(FleetRuleViolationException exception) {
    return problem(HttpStatus.CONFLICT, "Business rule violation", exception);
  }

  @ExceptionHandler(InvalidFleetValueException.class)
  ProblemDetail handleInvalidValue(InvalidFleetValueException exception) {
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

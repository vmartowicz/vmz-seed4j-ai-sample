package fr.vmz.seed4j.sample.ai.intermediate.parking.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.intermediate.parking.domain.ParkingRuleViolationException;
import fr.vmz.seed4j.sample.ai.intermediate.parking.domain.UnknownParkingElementException;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackageClasses = ParkingErrorsHandler.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
class ParkingErrorsHandler {

  @ExceptionHandler(UnknownParkingElementException.class)
  ProblemDetail handleUnknownElement(UnknownParkingElementException exception) {
    return problem(HttpStatus.NOT_FOUND, "Unknown element", exception);
  }

  @ExceptionHandler(ParkingRuleViolationException.class)
  ProblemDetail handleRuleViolation(ParkingRuleViolationException exception) {
    return problem(HttpStatus.CONFLICT, "Business rule violation", exception);
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

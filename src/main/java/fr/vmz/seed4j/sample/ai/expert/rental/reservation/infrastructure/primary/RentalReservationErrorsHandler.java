package fr.vmz.seed4j.sample.ai.expert.rental.reservation.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain.InvalidRentalReservationValueException;
import fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain.RentalReservationRuleViolationException;
import fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain.UnknownRentalReservationElementException;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackageClasses = RentalReservationErrorsHandler.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
class RentalReservationErrorsHandler {

  @ExceptionHandler(UnknownRentalReservationElementException.class)
  ProblemDetail handleUnknownElement(UnknownRentalReservationElementException exception) {
    return problem(HttpStatus.NOT_FOUND, "Unknown element", exception);
  }

  @ExceptionHandler(RentalReservationRuleViolationException.class)
  ProblemDetail handleRuleViolation(RentalReservationRuleViolationException exception) {
    return problem(HttpStatus.CONFLICT, "Business rule violation", exception);
  }

  @ExceptionHandler(InvalidRentalReservationValueException.class)
  ProblemDetail handleInvalidValue(InvalidRentalReservationValueException exception) {
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

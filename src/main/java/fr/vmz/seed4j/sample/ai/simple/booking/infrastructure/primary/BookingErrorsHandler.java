package fr.vmz.seed4j.sample.ai.simple.booking.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.simple.booking.domain.BookingRuleViolationException;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.InvalidBookingValueException;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.UnknownBookingElementException;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackageClasses = BookingErrorsHandler.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
class BookingErrorsHandler {

  @ExceptionHandler(UnknownBookingElementException.class)
  ProblemDetail handleUnknownElement(UnknownBookingElementException exception) {
    return problem(HttpStatus.NOT_FOUND, "Unknown element", exception);
  }

  @ExceptionHandler(BookingRuleViolationException.class)
  ProblemDetail handleRuleViolation(BookingRuleViolationException exception) {
    return problem(HttpStatus.CONFLICT, "Business rule violation", exception);
  }

  @ExceptionHandler(InvalidBookingValueException.class)
  ProblemDetail handleInvalidValue(InvalidBookingValueException exception) {
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

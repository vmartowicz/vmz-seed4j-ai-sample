package fr.vmz.seed4j.sample.ai.intermediate.clinic.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.ClinicRuleViolationException;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.InvalidClinicValueException;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.UnknownClinicElementException;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackageClasses = ClinicErrorsHandler.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
class ClinicErrorsHandler {

  @ExceptionHandler(UnknownClinicElementException.class)
  ProblemDetail handleUnknownElement(UnknownClinicElementException exception) {
    return problem(HttpStatus.NOT_FOUND, "Unknown element", exception);
  }

  @ExceptionHandler(ClinicRuleViolationException.class)
  ProblemDetail handleRuleViolation(ClinicRuleViolationException exception) {
    return problem(HttpStatus.CONFLICT, "Business rule violation", exception);
  }

  @ExceptionHandler(InvalidClinicValueException.class)
  ProblemDetail handleInvalidValue(InvalidClinicValueException exception) {
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

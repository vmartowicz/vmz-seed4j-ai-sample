package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.EcommerceRuleViolationException;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.InvalidEcommerceValueException;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.UnknownEcommerceElementException;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackageClasses = EcommerceErrorsHandler.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
class EcommerceErrorsHandler {

  @ExceptionHandler(UnknownEcommerceElementException.class)
  ProblemDetail handleUnknownElement(UnknownEcommerceElementException exception) {
    return problem(HttpStatus.NOT_FOUND, "Unknown element", exception);
  }

  @ExceptionHandler(EcommerceRuleViolationException.class)
  ProblemDetail handleRuleViolation(EcommerceRuleViolationException exception) {
    return problem(HttpStatus.CONFLICT, "Business rule violation", exception);
  }

  @ExceptionHandler(InvalidEcommerceValueException.class)
  ProblemDetail handleInvalidValue(InvalidEcommerceValueException exception) {
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

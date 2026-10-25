package fr.vmz.seed4j.sample.ai.advanced.restaurant.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.RestaurantRuleViolationException;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.InvalidRestaurantValueException;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.UnknownRestaurantElementException;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackageClasses = RestaurantErrorsHandler.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
class RestaurantErrorsHandler {

  @ExceptionHandler(UnknownRestaurantElementException.class)
  ProblemDetail handleUnknownElement(UnknownRestaurantElementException exception) {
    return problem(HttpStatus.NOT_FOUND, "Unknown element", exception);
  }

  @ExceptionHandler(RestaurantRuleViolationException.class)
  ProblemDetail handleRuleViolation(RestaurantRuleViolationException exception) {
    return problem(HttpStatus.CONFLICT, "Business rule violation", exception);
  }

  @ExceptionHandler(InvalidRestaurantValueException.class)
  ProblemDetail handleInvalidValue(InvalidRestaurantValueException exception) {
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

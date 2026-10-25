package fr.vmz.seed4j.sample.ai.advanced.shop.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.ShopRuleViolationException;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.InvalidShopValueException;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.UnknownShopElementException;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackageClasses = ShopErrorsHandler.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
class ShopErrorsHandler {

  @ExceptionHandler(UnknownShopElementException.class)
  ProblemDetail handleUnknownElement(UnknownShopElementException exception) {
    return problem(HttpStatus.NOT_FOUND, "Unknown element", exception);
  }

  @ExceptionHandler(ShopRuleViolationException.class)
  ProblemDetail handleRuleViolation(ShopRuleViolationException exception) {
    return problem(HttpStatus.CONFLICT, "Business rule violation", exception);
  }

  @ExceptionHandler(InvalidShopValueException.class)
  ProblemDetail handleInvalidValue(InvalidShopValueException exception) {
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

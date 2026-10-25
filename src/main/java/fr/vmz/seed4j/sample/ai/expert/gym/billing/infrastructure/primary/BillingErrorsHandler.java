package fr.vmz.seed4j.sample.ai.expert.gym.billing.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.gym.billing.domain.BillingRuleViolationException;
import fr.vmz.seed4j.sample.ai.expert.gym.billing.domain.UnknownBillingElementException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackageClasses = BillingErrorsHandler.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
class BillingErrorsHandler {

  @ExceptionHandler(UnknownBillingElementException.class)
  ProblemDetail handleUnknownElement(UnknownBillingElementException exception) {
    return problem(HttpStatus.NOT_FOUND, "Unknown element", exception);
  }

  @ExceptionHandler(BillingRuleViolationException.class)
  ProblemDetail handleRuleViolation(BillingRuleViolationException exception) {
    return problem(HttpStatus.CONFLICT, "Business rule violation", exception);
  }

  private ProblemDetail problem(HttpStatus status, String title, RuntimeException exception) {
    var problem = ProblemDetail.forStatusAndDetail(status, exception.getMessage());
    problem.setTitle(title);

    return problem;
  }
}

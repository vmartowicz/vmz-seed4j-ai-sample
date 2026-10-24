package fr.vmz.seed4j.sample.ai.simple.bank.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.simple.bank.domain.BankRuleViolationException;
import fr.vmz.seed4j.sample.ai.simple.bank.domain.InvalidBankValueException;
import fr.vmz.seed4j.sample.ai.simple.bank.domain.UnknownBankElementException;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackageClasses = BankErrorsHandler.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
class BankErrorsHandler {

  @ExceptionHandler(UnknownBankElementException.class)
  ProblemDetail handleUnknownElement(UnknownBankElementException exception) {
    return problem(HttpStatus.NOT_FOUND, "Unknown element", exception);
  }

  @ExceptionHandler(BankRuleViolationException.class)
  ProblemDetail handleRuleViolation(BankRuleViolationException exception) {
    return problem(HttpStatus.CONFLICT, "Business rule violation", exception);
  }

  @ExceptionHandler(InvalidBankValueException.class)
  ProblemDetail handleInvalidValue(InvalidBankValueException exception) {
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

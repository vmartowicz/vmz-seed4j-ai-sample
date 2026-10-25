package fr.vmz.seed4j.sample.ai.advanced.library.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.library.domain.LibraryRuleViolationException;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.InvalidLibraryValueException;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.UnknownLibraryElementException;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackageClasses = LibraryErrorsHandler.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
class LibraryErrorsHandler {

  @ExceptionHandler(UnknownLibraryElementException.class)
  ProblemDetail handleUnknownElement(UnknownLibraryElementException exception) {
    return problem(HttpStatus.NOT_FOUND, "Unknown element", exception);
  }

  @ExceptionHandler(LibraryRuleViolationException.class)
  ProblemDetail handleRuleViolation(LibraryRuleViolationException exception) {
    return problem(HttpStatus.CONFLICT, "Business rule violation", exception);
  }

  @ExceptionHandler(InvalidLibraryValueException.class)
  ProblemDetail handleInvalidValue(InvalidLibraryValueException exception) {
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

package fr.vmz.seed4j.sample.ai.expert.conference.program.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.program.domain.InvalidProgramValueException;
import fr.vmz.seed4j.sample.ai.expert.conference.program.domain.ProgramRuleViolationException;
import fr.vmz.seed4j.sample.ai.expert.conference.program.domain.UnknownProgramElementException;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackageClasses = ProgramErrorsHandler.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
class ProgramErrorsHandler {

  @ExceptionHandler(UnknownProgramElementException.class)
  ProblemDetail handleUnknownElement(UnknownProgramElementException exception) {
    return problem(HttpStatus.NOT_FOUND, "Unknown element", exception);
  }

  @ExceptionHandler(ProgramRuleViolationException.class)
  ProblemDetail handleRuleViolation(ProgramRuleViolationException exception) {
    return problem(HttpStatus.CONFLICT, "Business rule violation", exception);
  }

  @ExceptionHandler(InvalidProgramValueException.class)
  ProblemDetail handleInvalidValue(InvalidProgramValueException exception) {
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

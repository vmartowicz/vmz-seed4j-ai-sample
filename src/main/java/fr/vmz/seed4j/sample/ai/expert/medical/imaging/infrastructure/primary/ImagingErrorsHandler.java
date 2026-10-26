package fr.vmz.seed4j.sample.ai.expert.medical.imaging.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.ImagingRuleViolationException;
import fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.UnknownImagingElementException;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackageClasses = ImagingErrorsHandler.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
class ImagingErrorsHandler {

  @ExceptionHandler(UnknownImagingElementException.class)
  ProblemDetail handleUnknownElement(UnknownImagingElementException exception) {
    return problem(HttpStatus.NOT_FOUND, "Unknown element", exception);
  }

  @ExceptionHandler(ImagingRuleViolationException.class)
  ProblemDetail handleRuleViolation(ImagingRuleViolationException exception) {
    return problem(HttpStatus.CONFLICT, "Business rule violation", exception);
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

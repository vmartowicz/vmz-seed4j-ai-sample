package fr.vmz.seed4j.sample.ai.expert.medical.billing.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.medical.billing.domain.UnknownMedicalBillingElementException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackageClasses = MedicalBillingErrorsHandler.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
class MedicalBillingErrorsHandler {

  @ExceptionHandler(UnknownMedicalBillingElementException.class)
  ProblemDetail handleUnknownElement(UnknownMedicalBillingElementException exception) {
    return problem(HttpStatus.NOT_FOUND, "Unknown element", exception);
  }

  private ProblemDetail problem(HttpStatus status, String title, RuntimeException exception) {
    var problem = ProblemDetail.forStatusAndDetail(status, exception.getMessage());
    problem.setTitle(title);

    return problem;
  }
}

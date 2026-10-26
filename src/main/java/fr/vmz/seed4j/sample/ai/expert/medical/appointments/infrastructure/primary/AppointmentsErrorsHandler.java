package fr.vmz.seed4j.sample.ai.expert.medical.appointments.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.AppointmentsRuleViolationException;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.InvalidAppointmentsValueException;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.UnknownAppointmentsElementException;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackageClasses = AppointmentsErrorsHandler.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
class AppointmentsErrorsHandler {

  @ExceptionHandler(UnknownAppointmentsElementException.class)
  ProblemDetail handleUnknownElement(UnknownAppointmentsElementException exception) {
    return problem(HttpStatus.NOT_FOUND, "Unknown element", exception);
  }

  @ExceptionHandler(AppointmentsRuleViolationException.class)
  ProblemDetail handleRuleViolation(AppointmentsRuleViolationException exception) {
    return problem(HttpStatus.CONFLICT, "Business rule violation", exception);
  }

  @ExceptionHandler(InvalidAppointmentsValueException.class)
  ProblemDetail handleInvalidValue(InvalidAppointmentsValueException exception) {
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

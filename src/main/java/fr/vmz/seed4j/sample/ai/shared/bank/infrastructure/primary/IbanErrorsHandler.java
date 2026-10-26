package fr.vmz.seed4j.sample.ai.shared.bank.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.InvalidIbanException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class IbanErrorsHandler {

  @ExceptionHandler(InvalidIbanException.class)
  ProblemDetail handleInvalidIban(InvalidIbanException exception) {
    var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    problem.setTitle("Invalid value");

    return problem;
  }
}

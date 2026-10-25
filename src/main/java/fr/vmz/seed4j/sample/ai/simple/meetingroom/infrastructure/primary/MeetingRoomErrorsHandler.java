package fr.vmz.seed4j.sample.ai.simple.meetingroom.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.MeetingRoomRuleViolationException;
import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.InvalidMeetingRoomValueException;
import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.UnknownMeetingRoomElementException;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackageClasses = MeetingRoomErrorsHandler.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
class MeetingRoomErrorsHandler {

  @ExceptionHandler(UnknownMeetingRoomElementException.class)
  ProblemDetail handleUnknownElement(UnknownMeetingRoomElementException exception) {
    return problem(HttpStatus.NOT_FOUND, "Unknown element", exception);
  }

  @ExceptionHandler(MeetingRoomRuleViolationException.class)
  ProblemDetail handleRuleViolation(MeetingRoomRuleViolationException exception) {
    return problem(HttpStatus.CONFLICT, "Business rule violation", exception);
  }

  @ExceptionHandler(InvalidMeetingRoomValueException.class)
  ProblemDetail handleInvalidValue(InvalidMeetingRoomValueException exception) {
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

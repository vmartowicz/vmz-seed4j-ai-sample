package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.application.TicketingApplicationService;
import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.TicketNumber;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/conference/workshops/{workshopId}/registrations")
class WorkshopRegistrationsResource {

  private final TicketingApplicationService ticketing;

  WorkshopRegistrationsResource(TicketingApplicationService ticketing) {
    this.ticketing = ticketing;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  RestRegistration register(@PathVariable UUID workshopId, @RequestBody RestRegistrationRequest request) {
    return RestRegistration.from(ticketing.register(workshopId, request.toDomain()));
  }

  @PostMapping("/{ticket}/cancellation")
  void cancel(@PathVariable UUID workshopId, @PathVariable String ticket) {
    ticketing.cancelRegistration(workshopId, new TicketNumber(ticket));
  }

  @GetMapping
  List<RestRegistration> roster(@PathVariable UUID workshopId) {
    return ticketing.roster(workshopId).stream().map(RestRegistration::from).toList();
  }
}

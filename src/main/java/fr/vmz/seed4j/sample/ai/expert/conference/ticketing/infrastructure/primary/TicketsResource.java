package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.application.TicketingApplicationService;
import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.TicketNumber;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/conference/tickets")
class TicketsResource {

  private final TicketingApplicationService ticketing;

  TicketsResource(TicketingApplicationService ticketing) {
    this.ticketing = ticketing;
  }

  @PostMapping("/orders")
  @ResponseStatus(HttpStatus.CREATED)
  RestOrder order(@RequestBody RestOrderToPlace order) {
    return RestOrder.from(ticketing.order(order.toDomain()));
  }

  @GetMapping("/{number}")
  RestTicket ticket(@PathVariable String number) {
    return RestTicket.from(ticketing.ticket(new TicketNumber(number)));
  }

  @GetMapping
  List<RestTicket> ticketsOf(@RequestParam String holder) {
    return ticketing.ticketsOf(new Email(holder)).stream().map(RestTicket::from).toList();
  }

  @PostMapping("/{number}/name-change")
  RestTicket rename(@PathVariable String number, @RequestBody RestHolder holder) {
    return RestTicket.from(ticketing.rename(new TicketNumber(number), holder.toDomain()));
  }

  @PostMapping("/{number}/refund")
  RestTicket refund(@PathVariable String number) {
    return RestTicket.from(ticketing.refund(new TicketNumber(number)));
  }
}

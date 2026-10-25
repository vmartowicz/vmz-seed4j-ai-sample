package fr.vmz.seed4j.sample.ai.expert.conference.onsite.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.onsite.application.OnsiteApplicationService;
import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.BadgeId;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Room;
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
@RequestMapping("/api/conference/onsite")
class OnsiteResource {

  private final OnsiteApplicationService onsite;

  OnsiteResource(OnsiteApplicationService onsite) {
    this.onsite = onsite;
  }

  @PostMapping("/badges")
  @ResponseStatus(HttpStatus.CREATED)
  RestConferenceBadge printBadge(@RequestBody RestBadgeRequest request) {
    return RestConferenceBadge.from(onsite.printBadge(request.ticketNumber()));
  }

  @PostMapping("/volunteers")
  @ResponseStatus(HttpStatus.CREATED)
  RestConferenceBadge volunteerBadge(@RequestBody RestBadgeRequest request) {
    return RestConferenceBadge.from(onsite.volunteerBadge(request.volunteerName()));
  }

  @GetMapping("/badges/{badgeId}")
  RestConferenceBadge badge(@PathVariable UUID badgeId) {
    return RestConferenceBadge.from(onsite.badge(new BadgeId(badgeId)));
  }

  @PostMapping("/rooms/{room}/entries")
  RestRoomEntry enter(@PathVariable Room room, @RequestBody RestBadgeRequest request) {
    return RestRoomEntry.from(onsite.enter(request.badge(), room));
  }

  @PostMapping("/exits")
  RestRoomPresence exit(@RequestBody RestBadgeRequest request) {
    return RestRoomPresence.from(onsite.exit(request.badge()));
  }

  @GetMapping("/rooms")
  List<RestRoomOccupancy> occupancy() {
    return onsite.occupancy().stream().map(RestRoomOccupancy::from).toList();
  }

  @GetMapping("/attendance")
  List<RestAttendance> mostAttended() {
    return onsite.mostAttended().stream().map(RestAttendance::from).toList();
  }
}

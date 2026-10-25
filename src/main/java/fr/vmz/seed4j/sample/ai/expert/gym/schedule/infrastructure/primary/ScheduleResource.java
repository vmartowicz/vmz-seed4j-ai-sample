package fr.vmz.seed4j.sample.ai.expert.gym.schedule.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.gym.schedule.application.ScheduleApplicationService;
import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.SessionId;
import java.time.LocalDate;
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
@RequestMapping("/api/gym/schedule")
class ScheduleResource {

  private final ScheduleApplicationService schedule;

  ScheduleResource(ScheduleApplicationService schedule) {
    this.schedule = schedule;
  }

  @PostMapping("/coaches")
  @ResponseStatus(HttpStatus.CREATED)
  RestCoach register(@RequestBody RestCoachToRegister coach) {
    return RestCoach.from(schedule.register(coach.toDomain()));
  }

  @PostMapping("/classes")
  @ResponseStatus(HttpStatus.CREATED)
  RestGymClass schedule(@RequestBody RestClassToSchedule gymClass) {
    return RestGymClass.from(schedule.schedule(gymClass.toDomain()));
  }

  @PostMapping("/weeks/{monday}/publication")
  List<RestClassSession> publish(@PathVariable LocalDate monday) {
    return schedule.publish(monday).stream().map(RestClassSession::from).toList();
  }

  @GetMapping("/weeks/{monday}/sessions")
  List<RestClassSession> weekSessions(@PathVariable LocalDate monday) {
    return schedule.weekSessions(monday).stream().map(RestClassSession::from).toList();
  }

  @PostMapping("/sessions/{sessionId}/coach-replacement")
  RestClassSession replaceCoach(@PathVariable UUID sessionId, @RequestBody RestCoachReplacement replacement) {
    return RestClassSession.from(schedule.replaceCoach(new SessionId(sessionId), replacement.coach()));
  }

  @PostMapping("/sessions/{sessionId}/cancellation")
  RestClassSession cancel(@PathVariable UUID sessionId) {
    return RestClassSession.from(schedule.cancel(new SessionId(sessionId)));
  }
}

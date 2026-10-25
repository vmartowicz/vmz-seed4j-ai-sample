package fr.vmz.seed4j.sample.ai.expert.gym.schedule.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.gym.schedule.application.ScheduleApplicationService;
import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.SessionId;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class JavaClassSessions {

  private final ScheduleApplicationService schedule;

  public JavaClassSessions(ScheduleApplicationService schedule) {
    this.schedule = schedule;
  }

  public Optional<JavaClassSession> session(UUID session) {
    return schedule.findSession(new SessionId(session)).map(JavaClassSession::from);
  }
}

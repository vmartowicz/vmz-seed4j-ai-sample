package fr.vmz.seed4j.sample.ai.expert.gym.booking.domain;

import java.util.Optional;

public interface BookableSessions {
  Optional<BookableSession> session(SessionId session);
}

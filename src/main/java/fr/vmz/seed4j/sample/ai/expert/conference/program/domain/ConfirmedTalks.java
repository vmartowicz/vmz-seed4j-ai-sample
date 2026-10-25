package fr.vmz.seed4j.sample.ai.expert.conference.program.domain;

import java.util.Optional;
import java.util.UUID;

public interface ConfirmedTalks {
  Optional<TalkToSchedule> talk(UUID talk);
}

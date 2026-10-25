package fr.vmz.seed4j.sample.ai.expert.conference.program.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import java.util.List;
import java.util.UUID;

public record TalkToSchedule(UUID talk, String title, SessionFormat format, Track track, List<Email> speakers, boolean speakerFromAbroad) {
  boolean isWorkshop() {
    return format == SessionFormat.WORKSHOP;
  }
}
